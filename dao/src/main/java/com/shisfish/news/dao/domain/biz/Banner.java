package com.shisfish.news.dao.domain.biz;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.shisfish.news.dao.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author shisfish
 * @date 2023/8/23
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "banner")
public class Banner extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3099058557490237945L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "image_url")
    private String imageUrl;

    /**
     * 审核状态（0.审核中 1.审核成功 2.审核失败）
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(value = "order_num")
    private Integer orderNum;

}
