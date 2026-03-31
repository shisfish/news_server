package com.shisfish.news.dao.domain.biz;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.shisfish.news.dao.domain.BaseEntity;
import com.shisfish.news.dao.domain.biz.News;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

/**
 * 新闻评论表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "news_comment")
public class NewsComment extends BaseEntity implements Serializable {
    /**
     * 新闻评论id
     */
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Long commentId;

    /**
     * 新闻id
     */
    @TableField(value = "news_id")
    private Long newsId;

    /**
     * 新闻评论内容
     */
    @TableField(value = "comment_content")
    private String commentContent;

    /**
     * 点赞数
     */
    @TableField(value = "thumbs")
    private Long thumbs;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
//    @TableLogic
    @TableField(value = "del_flag")
    private Integer delFlag;

    /////////////////////////////////// 附加  ///////////////////////////////////
    @TableField(exist = false)
    private News news;

    /////////////////////////////////// 附加  ///////////////////////////////////
}