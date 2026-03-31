package com.shisfish.news.common.result.code;


import lombok.AllArgsConstructor;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 状态枚举类
 * @Version: 1.0.0
 */
@AllArgsConstructor
public enum ResponseCode implements ResponseCodeInterface {
    /**
     * 成功             OK                 200
     * 失败             ERROR              500
     */
    OK(200, "操作成功"),
    UPDATE_PASSWORD(201, "密码长时间未修改，请修改"),
    ERROR(500, "操作失败"),
    ;
    private final int code;

    private final String msg;


    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public int getCode() {
        return code;
    }
}
