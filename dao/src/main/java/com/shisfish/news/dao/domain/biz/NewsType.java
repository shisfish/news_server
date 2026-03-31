package com.shisfish.news.dao.domain.biz;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.shisfish.news.dao.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

/**
 * 新闻栏目表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "news_type")
public class NewsType extends BaseEntity implements Serializable {
    /**
     * 新闻栏目id
     */
    @TableId(value = "news_type_id", type = IdType.AUTO)
    private Long newsTypeId;

    /**
     * 新闻栏目名称
     */
    @NotBlank(message = "新闻栏目名称不能为空")
    @Size(min = 0, max = 30, message = "新闻栏目名称长度不能超过30个字符")
    @TableField(value = "news_type_name")
    private String newsTypeName;

    /**
     * 新闻栏目状态（0正常 1关闭）
     */
    @TableField(value = "status")
    private String status;

    /**
     * 链接显示顺序
     */
    @TableField(value = "order_num")
    private Integer orderNum;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
//    @TableLogic
    @TableField(value = "del_flag")
    private Integer delFlag;
}