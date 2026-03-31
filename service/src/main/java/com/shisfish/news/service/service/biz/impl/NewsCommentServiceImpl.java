package com.shisfish.news.service.service.biz.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.dao.domain.biz.NewsComment;
import com.shisfish.news.service.mapper.biz.NewsCommentMapper;
import com.shisfish.news.service.service.biz.INewsCommentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

@Service
public class NewsCommentServiceImpl extends ServiceImpl<NewsCommentMapper, NewsComment> implements INewsCommentService {
    /**
     * 条件分页查询新闻评论列表
     *
     * @param newsComment 新闻评论
     * @return 新闻评论集合
     */
    @Override
    public IPage<NewsComment> findPage(Page<NewsComment> newsCommentPage, NewsComment newsComment) {
        return baseMapper.findPage(newsCommentPage, newsComment);
    }

    /**
     * 查询新闻评论
     *
     * @param commentId 新闻评论ID
     * @return 新闻评论
     */
    @Override
    public NewsComment findNewsCommentById(Long commentId) {
        return getById(commentId);
    }

    /**
     * 新增新闻评论
     *
     * @param newsComment 新闻评论
     * @return 结果
     */
    @Override
    public boolean insertNewsComment(NewsComment newsComment) {
        return saveOrUpdate(newsComment);
    }

    /**
     * 修改新闻评论
     *
     * @param newsComment 新闻评论
     * @return 结果
     */
    @Override
    public boolean updateNewsComment(NewsComment newsComment) {
        return saveOrUpdate(newsComment);
    }

    /**
     * 批量删除新闻评论
     *
     * @param commentIds 需要删除的新闻评论ID
     * @return 结果
     */
    @Override
    public boolean deleteNewsCommentByIds(List<Long> commentIds) {
        return removeByIds(commentIds);
    }
}
