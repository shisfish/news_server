package com.shisfish.news.common.core.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 登录信息
 * @Version: 1.0.0
 */
@Data
public class LoginBody {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String code;

    private String uuid = "";

}
