package com.shisfish.news.dao.domain.biz;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

/**
 * 新闻表
 * 这个需要用到爬虫 所以特殊处理
 * createBy createTime updateBy updateTime 不用mybatis plus 进行自动填充 避免存入数据库失败
 */
@Data
@TableName(value = "news")
public class News implements Serializable {
    private static final long serialVersionUID = -6335036899932042925L;
    /**
     * 新闻id
     */
    @TableId(value = "news_id", type = IdType.AUTO)
    private Long newsId;
    /**
     * 作者id
     */
    @TableField(value = "user_id")
    private Long userId;
    /**
     * 新闻栏目id
     */
    @TableField(value = "news_type_id")
    private Long newsTypeId;

    /**
     * 类型：1 自建，默认；2.外链，跳转形式
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 新闻标题
     */
    @NotBlank(message = "新闻标题不能为空")
    @TableField(value = "news_title")
    private String newsTitle;

    /**
     * 新闻内容
     */
    @NotBlank(message = "新闻内容不能为空")
    @TableField(value = "news_content")
    private String newsContent;

    /**
     * 专题
     */
    @TableField(value = "subject")
    private Integer subject;

    /**
     * 新闻属性 0.头条区新闻 1.幻灯片区新闻 2.热点区新闻
     */
    @TableField(value = "news_attr")
    private String newsAttr;

    /**
     * 新闻封面
     */
    @TableField(value = "news_image")
    private String newsImage;

    /**
     * 新闻显示顺序
     */
    @TableField(value = "order_num")
    private Integer orderNum;

    /**
     * 新闻是否公开（0.公开 1.私有）
     */
    @TableField(value = "is_public")
    private Integer isPublic;

    /**
     * 新闻点赞数
     */
    @TableField(value = "thumbs")
    private Long thumbs;

    /**
     * 新闻浏览量
     */
    @TableField(value = "visits")
    private Long visits;

    /**
     * 新闻评论数
     */
    @TableField(value = "comments")
    private Long comments;

    /**
     * 新闻收藏数
     */
    @TableField(value = "collects")
    private Long collects;
    /**
     * 新闻来源 WECHAT SYSTEM
     */
    @TableField(value = "news_source")
    private String newsSource;
    /**
     * 新闻来源原创作者(博客使用)
     */
    @TableField(value = "news_source_author")
    private String newsSourceAuthor;

    /**
     * 新闻博客分类标签
     */
    @TableField(value = "news_source_tags")
    private String newsSourceTags;

    /**
     * 审核状态（0.审核中 1.审核成功 2.审核失败）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 是否静态化了，0未静态化，1已静态化
     */
    @TableField(value = "to_static")
    private Integer toStatic;


    /**
     * 静态资源所在地址
     */
    @TableField(value = "static_file_path")
    private String staticFilePath;

    /**
     * 外链
     */
    @TableField(value = "link")
    private String link;


    /**
     * 发布时间
     */
    @TableField(value = "publish_time")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date publishTime;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
//    @TableLogic
    @TableField(value = "del_flag")
    private Integer delFlag;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 创建者
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date updateTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /////////////////////////////////// 附加  //////////////////////////////////////////////////////////////////////

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

    /////////////////////////////////// 附加  ///////////////////////////////////

    @TableField(exist = false)
    private NewsType newsType;

    /////////////////////////////////// 附加  ///////////////////////////////////
}