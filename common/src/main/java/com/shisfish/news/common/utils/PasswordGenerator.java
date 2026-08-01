package com.shisfish.news.common.utils;

import java.security.SecureRandom;

/**
 * 随机强密码生成工具类
 */
public class PasswordGenerator {

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    private static final String ALL = LOWER + UPPER + DIGITS + SPECIAL;
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 生成16位随机强密码
     */
    public static String generate() {
        StringBuilder sb = new StringBuilder(16);
        sb.append(LOWER.charAt(RANDOM.nextInt(LOWER.length())));
        sb.append(UPPER.charAt(RANDOM.nextInt(UPPER.length())));
        sb.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        sb.append(SPECIAL.charAt(RANDOM.nextInt(SPECIAL.length())));
        for (int i = 4; i < 16; i++) {
            sb.append(ALL.charAt(RANDOM.nextInt(ALL.length())));
        }
        return shuffle(sb.toString());
    }

    private static String shuffle(String input) {
        char[] chars = input.toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        return new String(chars);
    }
}
