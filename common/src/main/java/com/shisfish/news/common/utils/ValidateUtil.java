package com.shisfish.news.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: shisfish
 * @Date: 2023/9/18
 * @Description: 验证规则工具
 * @Version: 1.0.0
 */
public class ValidateUtil {
    /**
     * 邮件正则
     */
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";

    /**
     * 手机号正则
     */
    private static final String MOBILE_PATTERN = "^1[3-9]\\d{9}$";

    /**
     * 中文正则
     */
    private static final String CHINESE_PATTERN = "[\u4E00-\u9FA5]{1,}";

    /**
     * 包含中文正则
     */
    private static final String HAS_CHINESE_PATTERN = "[\\s\\S]*[\u4E00-\u9FA5]+[\\s\\S]*";

    /**
     * 验证邮件有效性
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    /**
     * 手机格式验证
     */
    public static boolean checkMobile(String mobile) {
        return mobile.matches(MOBILE_PATTERN);
    }

    /**
     * 验证中文
     *
     * @param chinese
     * @return
     */
    public static boolean checkChinese(String chinese) {
        return chinese.matches(CHINESE_PATTERN);
    }


    /**
     * 包含中文
     *
     * @param chinese
     * @return
     */
    public static boolean hashinese(String chinese) {
        return chinese.matches(HAS_CHINESE_PATTERN);
    }
}
