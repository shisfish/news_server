package com.shisfish.news.service.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shisfish.news.dao.domain.biz.NewsComment;

import java.util.List;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

public interface INewsCommentService extends IService<NewsComment> {
    /**
     * 条件分页查询新闻评论列表
     *
     * @param newsComment 新闻评论
     * @return 新闻评论集合
     */
    IPage<NewsComment> findPage(Page<NewsComment> newsCommentPage, NewsComment newsComment);

    /**
     * 查询新闻评论
     *
     * @param commentId 新闻评论ID
     * @return 新闻评论
     */
    NewsComment findNewsCommentById(Long commentId);

    /**
     * 新增新闻评论
     *
     * @param newsComment 新闻评论
     * @return 结果
     */
    boolean insertNewsComment(NewsComment newsComment);

    /**
     * 修改新闻评论
     *
     * @param newsComment 新闻评论
     * @return 结果
     */
    boolean updateNewsComment(NewsComment newsComment);

    /**
     * 批量删除新闻评论
     *
     * @param commentIds 需要删除的新闻评论ID
     * @return 结果
     */
    boolean deleteNewsCommentByIds(List<Long> commentIds);
}
