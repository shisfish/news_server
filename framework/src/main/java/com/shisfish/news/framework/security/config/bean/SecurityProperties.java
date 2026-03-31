package com.shisfish.news.framework.security.config.bean;

import lombok.Data;

/**
 * Jwt参数配置
 */
@Data
public class SecurityProperties {

    /**
     * Request Headers ： Authorization
     */
    private String header;

    /**
     * 令牌前缀，最后留个空格 Bearer
     */
    private String tokenStartWith;

    /**
     * 必须使用最少88位的Base64对该令牌进行编码
     */
    private String base64Secret;

    /**
     * 令牌过期时间 此处单位/分钟
     */
    private Long tokenValidityInMinutes;


    public String getTokenStartWith() {
        return tokenStartWith + " ";
    }
}
