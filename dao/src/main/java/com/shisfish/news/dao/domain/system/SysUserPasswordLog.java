package com.shisfish.news.dao.domain.system;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shisfish
 * @date 2023/8/29
 * @Description 日志更新记录
 */
@Data
public class SysUserPasswordLog implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "password")
    private String password;

    /**
     * 密码更新类型（1，新建用户，2，重置密码，用户更新密码）
     */
    @TableField(value = "type")
    private Integer type;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

}
