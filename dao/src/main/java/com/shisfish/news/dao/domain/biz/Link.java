package com.shisfish.news.dao.domain.biz;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.shisfish.news.dao.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

/**
 * 友情链接表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "link")
public class Link extends BaseEntity implements Serializable {
    /**
     * 友情链接id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotNull(message = "类型不能为空")
    @TableField(value = "link_type")
    private Integer linkType;

    /**
     * 友情链接名称
     */
    @NotBlank(message = "友情链接名称不能为空")
    @TableField(value = "link_name")
    private String linkName;

    /**
     * 友情链接地址
     */
    @NotBlank(message = "友情链接地址不能为空")
    @Size(min = 0, max = 30, message = "友情链接地址长度不能超过30个字符")
    @TableField(value = "link_url")
    private String linkUrl;

    /**
     * 联系人邮件
     */
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    @TableField(value = "email")
    private String email;

    /**
     * 链接显示顺序
     */
    @TableField(value = "order_num")
    private Integer orderNum;
}