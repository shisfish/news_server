package com.shisfish.news.common.utils;

import com.shisfish.news.common.utils.string.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 门户接口请求签名工具（HMAC-SHA256）。
 *
 * <p>签名原文：method + "\n" + path + "\n" + timestamp + "\n" + nonce
 * <ul>
 *     <li>path 统一去掉 /prod-api 前缀、且不含 query，规避 nginx 前缀 rewrite 造成前后端不一致；</li>
 *     <li>timestamp 为毫秒时间戳，后端容忍 ±5 分钟时钟偏移；</li>
 *     <li>nonce 本地去重（5 分钟窗口），防止同一签名被重放。</li>
 * </ul>
 * 注意：纯静态前端无法真正保密密钥，本机制仅提升伪造/重放门槛，不防逆向提取密钥。
 *
 * <p>实现说明（2026-11-17）：直接用 JDK 自带的 SunJCE 实现，<b>不要</b>换成 Hutool 的
 * {@code cn.hutool.crypto.digest.HMac}。Hutool 一旦在 classpath 上探测到 BouncyCastle，
 * 就会优先使用 BC provider，而 Spring Boot fat jar 里嵌套的 bcprov 签名无法通过 JCE 验签，
 * 运行时会抛 {@code SecurityException: JCE cannot authenticate the provider BC}
 * （本地 IDE 直接跑能过、打包上线必炸，已踩过的坑）。
 *
 * @author shisfish
 * @date 2026/11/16
 */
public class SignUtil {

    /** 签名算法：JDK 自带 provider 即可，不依赖 BouncyCastle */
    private static final String ALGORITHM = "HmacSHA256";

    /** 允许的时间偏移（毫秒） */
    private static final long WINDOW_MS = Duration.ofMinutes(5).toMillis();

    /** nginx 转发时可能保留的前缀，签名前统一去掉 */
    private static final String PROD_API_PREFIX = "/prod-api";

    /** nonce 去重缓存：nonce -> 首次校验时间(ms) */
    private static final Map<String, Long> NONCE_CACHE = new ConcurrentHashMap<>();

    private SignUtil() {
    }

    /**
     * 规范化参与签名的路径：去掉 /prod-api 前缀。
     *
     * @param uri 请求 URI（不含 query）
     * @return 规范化后的路径
     */
    public static String normalizePath(String uri) {
        if (uri == null) {
            return "";
        }
        if (uri.startsWith(PROD_API_PREFIX)) {
            return uri.substring(PROD_API_PREFIX.length());
        }
        return uri;
    }

    /**
     * 计算签名（小写 hex）。
     *
     * @param secret    密钥
     * @param method    HTTP 方法（大写）
     * @param path      请求 URI（不含 query，可带 /prod-api 前缀）
     * @param timestamp 毫秒时间戳字符串
     * @param nonce     本次请求唯一随机串
     * @return HMAC-SHA256 小写 hex 签名
     */
    public static String sign(String secret, String method, String path, String timestamp, String nonce) {
        String data = method + "\n" + normalizePath(path) + "\n" + timestamp + "\n" + nonce;
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM));
            return toHex(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            // 不吞异常：加密环境异常应当暴露成 500，而不是被误判成"签名不对"返回 401
            throw new IllegalStateException("HmacSHA256 计算失败：" + e.getMessage(), e);
        }
    }

    /**
     * 校验签名：时间窗口 + 签名值 + nonce 防重放。
     *
     * <p>注意：只有"业务上不通过"才返回 false；加密环境异常会直接抛出，
     * 避免 {@code JCE cannot authenticate the provider BC} 这类问题被降级成 401 而查不出原因。
     *
     * @param secret    密钥
     * @param method    HTTP 方法
     * @param path      请求 URI（不含 query）
     * @param timestamp 毫秒时间戳字符串
     * @param nonce     随机串
     * @param sign      待校验签名
     * @return 通过 true，否则 false
     */
    public static boolean verify(String secret, String method, String path, String timestamp, String nonce, String sign) {
        if (StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(nonce) || StringUtils.isEmpty(sign)) {
            return false;
        }
        long ts;
        try {
            ts = Long.parseLong(timestamp);
        } catch (NumberFormatException e) {
            return false;
        }
        long now = System.currentTimeMillis();
        if (Math.abs(now - ts) > WINDOW_MS) {
            return false;
        }
        String expected = sign(secret, method, path, timestamp, nonce);
        if (!expected.equalsIgnoreCase(sign)) {
            return false;
        }
        // 签名通过后再登记 nonce，避免错误请求污染去重缓存
        NONCE_CACHE.entrySet().removeIf(e -> e.getValue() < now - WINDOW_MS);
        return NONCE_CACHE.putIfAbsent(nonce, now) == null;
    }

    /**
     * 字节数组转小写 hex。
     *
     * @param bytes 字节数组
     * @return 小写 hex 字符串
     */
    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(Character.forDigit((b >> 4) & 0xF, 16));
            sb.append(Character.forDigit(b & 0xF, 16));
        }
        return sb.toString();
    }
}
