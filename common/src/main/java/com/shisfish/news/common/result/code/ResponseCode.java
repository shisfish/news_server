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
    PASSWORD_WILL_EXPIRE(202, "密码即将过期，请及时修改"),
    PASSWORD_EXPIRED(203, "密码已过期，请立即修改"),
    FIRST_LOGIN_RESET(204, "首次登录，请修改密码"),
    PASSWORD_COMPLEXITY_ERROR(301, "密码长度不少于12位，必须包含大写字母、小写字母、数字及特殊字符"),
    PASSWORD_CONTAINS_USERNAME(302, "密码不能包含用户名"),
    PASSWORD_TOO_SIMPLE(303, "密码过于简单，不能使用常见弱密码"),
    PASSWORD_REUSE_HISTORY(304, "新密码不能与最近3次历史密码相同"),
    PASSWORD_SAME_AS_OLD(305, "新密码不能与旧密码相同"),
    OLD_PASSWORD_ERROR(306, "修改密码失败，旧密码错误"),
    PASSWORD_EMPTY(307, "密码不能为空"),
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
