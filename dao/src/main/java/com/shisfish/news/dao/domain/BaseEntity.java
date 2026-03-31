package com.shisfish.news.dao.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: Entity基类
 * @Version: 1.0.0
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 4641485502187075953L;

    /**
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

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

    /**
     * 请求参数
     */
    @TableField(exist = false)
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    /////////////////////////////////// 附加  ///////////////////////////////////
}
