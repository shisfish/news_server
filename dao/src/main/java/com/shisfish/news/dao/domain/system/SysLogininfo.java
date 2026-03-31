package com.shisfish.news.dao.domain.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

/**
 * 系统访问记录
 */
@Data
@TableName(value = "sys_logininfo")
public class SysLogininfo implements Serializable {
    /**
     * 访问ID
     */
    @TableId(value = "info_id", type = IdType.AUTO)
    private Long infoId;

    /**
     * 用户账号
     */
    @TableField(value = "username")
    private String username;

    /**
     * 登录IP地址
     */
    @TableField(value = "ipaddr")
    private String ipaddr;

    /**
     * 登录地点
     */
    @TableField(value = "login_location")
    private String loginLocation;

    /**
     * 浏览器类型
     */
    @TableField(value = "browser")
    private String browser;

    /**
     * 操作系统
     */
    @TableField(value = "os")
    private String os;

    /**
     * 登录状态（0成功 1失败）
     */
    @TableField(value = "status")
    private String status;

    /**
     * 提示消息
     */
    @TableField(value = "msg")
    private String msg;

    /**
     * 访问时间
     */
    @TableField(value = "login_time")
    private Date loginTime;


    /////////////////////////////////// 附加  ///////////////////////////////////
    /**
     * 开始时间
     */
    @TableField(exist = false)
    @JsonIgnore
    private String beginTime;

    /**
     * 结束时间
     */
    @TableField(exist = false)
    @JsonIgnore
    private String endTime;

    /////////////////////////////////// 附加  ///////////////////////////////////
    private static final long serialVersionUID = 1L;
}