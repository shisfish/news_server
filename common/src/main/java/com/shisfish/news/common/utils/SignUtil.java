package com.shisfish.news.common.utils;

import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.shisfish.news.common.utils.string.StringUtils;

import java.nio.charset.StandardCharsets;
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
 * @author shisfish
 * @date 2026/11/16
 */
public class SignUtil {

    /** 允许的时间偏移（毫秒） */
    private static final long WINDOW_MS = 5 * 60 * 1000L;

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
        return new HMac(HmacAlgorithm.HmacSHA256, secret.getBytes(StandardCharsets.UTF_8))
                .digestHex(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 校验签名：时间窗口 + 签名值 + nonce 防重放。
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
        try {
            if (StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(nonce) || StringUtils.isEmpty(sign)) {
                return false;
            }
            long ts = Long.parseLong(timestamp);
            if (Math.abs(System.currentTimeMillis() - ts) > WINDOW_MS) {
                return false;
            }
            String expected = sign(secret, method, path, timestamp, nonce);
            if (!expected.equalsIgnoreCase(sign)) {
                return false;
            }
            // 签名通过后再登记 nonce，避免错误请求污染去重缓存
            long now = System.currentTimeMillis();
            NONCE_CACHE.entrySet().removeIf(e -> e.getValue() < now - WINDOW_MS);
            return NONCE_CACHE.putIfAbsent(nonce, now) == null;
        } catch (Exception e) {
            return false;
        }
    }
}
