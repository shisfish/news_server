package com.shisfish.news.dao.domain.biz;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
public class NewsBase implements Serializable {

    private Long newsId;

    private Long userId;

    private String newsTypeName;

    private String typeName;

    private String newsTitle;

    private String newsContent;

    private String subjectName;

    private String newsImage;

    private Integer orderNum;

    private String statusName;

    private String link;

    private Date publishTime;

    private Integer delFlag;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String remark;

}
