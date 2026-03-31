package com.shisfish.news.service.mapper.biz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.dao.domain.biz.NewsComment;
import org.apache.ibatis.annotations.Param;


/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

public interface NewsCommentMapper extends BaseMapper<NewsComment> {
    /**
     * 条件分页查询新闻评论列表
     *
     * @param comment 新闻评论
     *
     * @return 新闻评论集合
     */
    IPage<NewsComment> findPage(Page<NewsComment> newsCommentPage, @Param("comment") NewsComment comment);
}