package com.shisfish.news.framework.security.service;


import com.google.common.collect.Maps;
import com.shisfish.news.common.constant.RedisConstants;
import com.shisfish.news.common.core.EhcacheUtil;
import com.shisfish.news.common.core.domain.LoginUser;
//import com.shisfish.common.core.redis.RedisService;
import com.shisfish.news.common.utils.ServletUtils;
import com.shisfish.news.common.utils.string.StringUtils;
import com.shisfish.news.common.utils.uuid.IdUtils;
import com.shisfish.news.framework.security.config.bean.SecurityProperties;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: token验证处理
 * @Version: 1.0.0
 */
@Component
public class TokenService {

    @Autowired
    private SecurityProperties securityProperties;

//    @Autowired
//    private RedisService redisService;

    @Autowired
    private EhcacheUtil ehcacheUtil;
    /**
     * 毫秒
     */
    protected static final long MILLIS_SECOND = 1000;
    /**
     * 分钟
     */
    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    /**
     * 20分钟
     */
    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = parseToken(token);
            // 解析对应的权限以及用户信息
            String uuid = (String) claims.get(RedisConstants.LOGIN_USER_KEY);
            String userKey = getTokenKey(uuid);
//            return redisService.getCacheObject(userKey);
            return ehcacheUtil.getLoginUserCache(userKey);
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }


    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
//            redisService.deleteObject(userKey);
            ehcacheUtil.deleteLoginUserCache(userKey);
        }
    }


    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        setUserAgent(loginUser);
        refreshToken(loginUser);
        Map<String, Object> claims = Maps.newHashMapWithExpectedSize(1);
        claims.put(RedisConstants.LOGIN_USER_KEY, token);
        return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + securityProperties.getTokenValidityInMinutes() * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
//        redisService.setCacheObject(userKey, loginUser, securityProperties.getTokenValidityInMinutes(), TimeUnit.MINUTES);
        ehcacheUtil.setLoginUserCache(userKey, loginUser);
    }


    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = StringUtils.getIp(ServletUtils.getRequest());
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(StringUtils.getHttpCityInfo(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }


    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, securityProperties.getBase64Secret()).compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(securityProperties.getBase64Secret())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @param request /
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(securityProperties.getHeader());
        if (StringUtils.isNotEmpty(token) && token.startsWith(securityProperties.getTokenStartWith())) {
            token = token.replace(securityProperties.getTokenStartWith(), "");
        }
        return token;
    }

    private String getTokenKey(String uuid) {
        return RedisConstants.LOGIN_TOKEN_KEY + uuid;
    }
}
