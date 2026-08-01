package com.shisfish.news.common.utils;

import com.shisfish.news.common.exception.CustomException;
import com.shisfish.news.common.result.code.ResponseCode;
import com.shisfish.news.common.utils.string.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 密码复杂度校验工具类
 */
public class PasswordValidator {

    /**
     * 密码复杂度正则：长度≥12位，包含大写、小写、数字、特殊字符
     */
    private static final Pattern COMPLEXITY_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{12,}$");

    /**
     * 常见弱密码黑名单
     */
    private static final Set<String> WEAK_PASSWORD_BLACKLIST = new HashSet<>(Arrays.asList(
            "password", "123456", "12345678", "123456789", "qwerty", "admin", "root",
            "letmein", "welcome", "monkey", "dragon", "master", "abc123", "admin123",
            "password123", "login", "user", "test", "guest", "123qwe", "qwe123"
    ));

    /**
     * 校验密码复杂度
     *
     * @param password 明文密码
     * @param username 用户名（用于检查密码是否包含用户名）
     */
    public static void validate(String password, String username) {
        if (StringUtils.isEmpty(password)) {
            throw new CustomException(ResponseCode.PASSWORD_EMPTY.getMsg());
        }
        if (password.length() > 24) {
            throw new CustomException(ResponseCode.PASSWORD_COMPLEXITY_ERROR.getMsg());
        }
        if (!COMPLEXITY_PATTERN.matcher(password).matches()) {
            throw new CustomException(ResponseCode.PASSWORD_COMPLEXITY_ERROR.getMsg());
        }
        if (StringUtils.isNotEmpty(username) && password.toLowerCase().contains(username.toLowerCase())) {
            throw new CustomException(ResponseCode.PASSWORD_CONTAINS_USERNAME.getMsg());
        }
        String lowerPassword = password.toLowerCase();
        for (String weak : WEAK_PASSWORD_BLACKLIST) {
            if (lowerPassword.contains(weak)) {
                throw new CustomException(ResponseCode.PASSWORD_TOO_SIMPLE.getMsg());
            }
        }
    }
}
