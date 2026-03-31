package com.shisfish.news.common.core;

import com.shisfish.news.common.core.domain.LoginUser;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author shisfish
 * @date 2023/10/21
 * @Description
 */
@Component
public class EhcacheUtil {

    @Cacheable(value = "captchaCache", key = "#key")
    public String getCaptcha(String key) {
        return null;
    }

    @CachePut(value = "captchaCache", key = "#key")
    public String setCaptcha(String key, String value) {
        return value;
    }

    @CacheEvict(value = "captchaCache", key = "#key")
    public void deleteCaptcha(String key) {

    }

    @Cacheable(value = "loginLockCache", key = "#key")
    public Integer getLoginLockCache(String key) {
        return null;
    }

    @CachePut(value = "loginLockCache", key = "#key")
    public Integer setLoginLockCache(String key, Integer value) {
        return value;
    }

    @CacheEvict(value = "loginLockCache", key = "#key")
    public void deleteLoginLockCache(String key) {

    }

    @Cacheable(value = "loginFailureCountCache", key = "#key")
    public Integer getLoginFailureCountCache(String key) {
        return null;
    }

    @CachePut(value = "loginFailureCountCache", key = "#key")
    public Integer setLoginFailureCountCache(String key, Integer value) {
        return value;
    }

    @CacheEvict(value = "loginFailureCountCache", key = "#key")
    public void deleteLoginFailureCountCache(String key) {

    }

    @Cacheable(value = "loginUserCache", key = "#key")
    public LoginUser getLoginUserCache(String key) {
        return null;
    }

    @CachePut(value = "loginUserCache", key = "#key")
    public LoginUser setLoginUserCache(String key, LoginUser value) {
        return value;
    }

    @CacheEvict(value = "loginUserCache", key = "#key")
    public void deleteLoginUserCache(String key) {

    }

}
