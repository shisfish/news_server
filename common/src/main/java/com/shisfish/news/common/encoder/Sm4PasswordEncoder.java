package com.shisfish.news.common.encoder;

import cn.hutool.crypto.SmUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author shisfish
 * @date 2023/9/1
 * @Description 国密SM4
 */
public class Sm4PasswordEncoder implements PasswordEncoder {

    // key长度必须为16
    private static final String KEY = "JkuYGB51#skL45GH";

    @Override
    public String encode(CharSequence rawPassword) {
        return SmUtil.sm4(KEY.getBytes(StandardCharsets.UTF_8)).encryptHex(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return Objects.equals(rawPassword.toString(),
                SmUtil.sm4(KEY.getBytes(StandardCharsets.UTF_8)).decryptStr(encodedPassword, StandardCharsets.UTF_8));
    }
}
