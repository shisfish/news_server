package com.shisfish.news.service.mapper.biz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.dao.domain.biz.News;
import com.shisfish.news.dao.domain.biz.NewsBase;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

public interface NewsMapper extends BaseMapper<News> {
    /**
     * 查询新闻列表
     *
     * @param news 新闻
     *
     * @return 新闻集合
     */
    IPage<News> findPageAll(Page<News> newsPage, @Param("news") News news);

    /**
     * 根据用户id查询新闻列表
     *
     * @param news   新闻
     * @param userId 用户id
     *
     * @return 新闻集合
     */
    IPage<News> findPageByUserId(Page<News> newsPage, @Param("news") News news, @Param("userId") Long userId);

    IPage<News> findPageWithImage(Page<News> newsPage, @Param("news") News news);

    void saveNewsBase(@Param("tableString") String tableString, @Param("newsBase") NewsBase newsBase);
}