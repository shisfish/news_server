package com.shisfish.news.dao.domain.biz;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * 新闻收藏表
 */
@Data
@TableName(value = "news_collect")
public class NewsCollect implements Serializable {
    /**
     * 收藏id
     */
    @TableId(value = "collection_id", type = IdType.AUTO)
    private Long collectionId;

    /**
     * 新闻id
     */
    @TableField(value = "news_id")
    private Long newsId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 收藏时间
     */
    @TableField(value = "collect_time")
    private Date collectTime;

    private static final long serialVersionUID = 1L;
}