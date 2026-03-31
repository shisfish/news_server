package com.shisfish.news.service.mapper.biz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.dao.domain.biz.NewsType;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

public interface NewsTypeMapper extends BaseMapper<NewsType> {
    /**
     * 条件分页新闻栏目列表
     *
     * @param newsTypePage 分页插件
     * @param newsType         菜单类型
     *
     * @return 结果
     */
    IPage<NewsType> findPage(Page<NewsType> newsTypePage, @Param("newsType") NewsType newsType);
}