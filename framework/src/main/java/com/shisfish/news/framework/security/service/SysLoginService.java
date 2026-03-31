package com.shisfish.news.framework.security.service;


import com.shisfish.news.common.constant.Constants;
import com.shisfish.news.common.constant.RedisConstants;
import com.shisfish.news.common.core.EhcacheUtil;
import com.shisfish.news.common.core.domain.LoginUser;
//import com.shisfish.common.core.redis.RedisService;
import com.shisfish.news.common.exception.CustomException;
import com.shisfish.news.common.exception.user.CaptchaException;
import com.shisfish.news.common.exception.user.CaptchaExpireException;
import com.shisfish.news.common.exception.user.UserPasswordNotMatchException;
import com.shisfish.news.common.utils.MessageUtils;
import com.shisfish.news.common.utils.string.StringUtils;
import com.shisfish.news.framework.manager.AsyncManager;
import com.shisfish.news.framework.manager.factory.AsyncFactory;
import com.shisfish.news.framework.security.config.bean.LoginProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录校验方法
 */
@Component
public class SysLoginService {

    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

//    @Autowired
//    private RedisService redisService;

    @Autowired
    private EhcacheUtil ehcacheUtil;

    @Autowired
    private LoginProperties loginProperties;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        String verifyKey = RedisConstants.CAPTCHA_CODE_KEY + uuid;
        // 查询验证码
//        String captcha = redisService.getCacheObject(verifyKey);
        String captcha = ehcacheUtil.getCaptcha(verifyKey);
        // 清除验证码
//        redisService.deleteObject(verifyKey);
        ehcacheUtil.deleteCaptcha(verifyKey);
        if (StringUtils.isBlank(captcha)) {
            // 记录登录日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        // 验证码错误
        if (!code.equalsIgnoreCase(captcha)) {
            // 记录登录日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }

        Integer failureCount = loginProperties.getLock().getFailureCount();
        Integer lockTime = loginProperties.getLock().getLockTime();
        Integer failureExpireTime = loginProperties.getLock().getFailureExpireTime();

        // 验证ip是否已经锁定
        String lockUserNameKey = RedisConstants.LOGIN_LOCK_USERNAME + username;
        String failureCountKey = RedisConstants.LOGIN_FAILURE_COUNT_USERNAME + username;
//        Object lockIpObject = redisService.getCacheObject(lockUserNameKey);
        Integer lockIpObject = ehcacheUtil.getLoginLockCache(lockUserNameKey);
        String message = MessageUtils.message("user.password.failure.error", failureCount, lockTime);
        if (lockIpObject != null) {
            // 记录登录日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, message));
            throw new CustomException(message);
        }

        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                // 用户或密码错误,登录失败
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));

//                Object failureCountObject = redisService.getCacheObject(failureCountKey);
                Integer failureCountObject = ehcacheUtil.getLoginFailureCountCache(failureCountKey);
                int thisFailureCount = 1;
                if (failureCountObject != null) {
                    thisFailureCount = failureCountObject + 1;
                }
                if (thisFailureCount >= failureCount) {
                    // 创建锁定信息
//                    redisService.setCacheObject(lockUserNameKey, 0, lockTime, TimeUnit.MINUTES);
                    ehcacheUtil.setLoginLockCache(lockUserNameKey, 0);
                    // 返回给用户锁定信息
                    throw new CustomException(message);
                } else {
//                    redisService.setCacheObject(failureCountKey, thisFailureCount, failureExpireTime, TimeUnit.MINUTES);
                    ehcacheUtil.setLoginFailureCountCache(failureCountKey, thisFailureCount);
                    throw new UserPasswordNotMatchException();
                }
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new CustomException(e.getMessage());
            }
        }
        // 记录登录日志
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        // 删除锁定日志，失败次数日志
//        redisService.deleteObject(lockUserNameKey);
        ehcacheUtil.deleteLoginLockCache(lockUserNameKey);
//        redisService.deleteObject(failureCountKey);
        ehcacheUtil.deleteLoginFailureCountCache(lockUserNameKey);
        // 生成token
        return tokenService.createToken(loginUser);
    }
}
