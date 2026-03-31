package com.shisfish.news.framework.security.config.bean;

import lombok.Data;

/**
 * @author shisfish
 * @date 2023/9/7
 * @Description 锁定配置文件
 */
@Data
public class LoginLockProperties {

    /**
     * 失效时间 分钟（在xxx周期内失败次数）
     */
    private Integer failureExpireTime;

    /**
     * 失败次数
     */
    private Integer failureCount;

    /**
     * 锁定时间 分钟
     */
    private Integer lockTime;

}
