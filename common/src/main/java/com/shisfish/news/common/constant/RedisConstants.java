package com.shisfish.news.common.constant;

/**
 * @author shisfish
 * @date 2023/9/11
 * @Description Redis相关常量
 */
public class RedisConstants {

    private RedisConstants() {}

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key_:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_token_key_:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_img_code_:";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config_key_:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict_key_:";

    /**
     * ip锁定 redis key
     */
    public static final String LOGIN_LOCK_USERNAME = "login_lock_username_:";

    /**
     * ip锁定 redis key
     */
    public static final String LOGIN_FAILURE_COUNT_USERNAME = "login_failure_count_username_:";

}
