package com.shisfish.news.service.service.biz;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shisfish.news.dao.domain.biz.NewsType;

import java.util.List;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

public interface INewsTypeService extends IService<NewsType> {
    /**
     * 条件分页新闻栏目列表
     *
     * @param newsTypePage 分页插件
     * @param newsType     菜单栏目
     * @return 结果
     */
    IPage<NewsType> findPage(Page<NewsType> newsTypePage, NewsType newsType);

    /**
     * 查询新闻栏目
     *
     * @param newsTypeId 新闻栏目ID
     * @return 新闻栏目
     */
    NewsType findNewsTypeById(Long newsTypeId);

    /**
     * 新增新闻栏目
     *
     * @param newsType 新闻栏目
     * @return 结果
     */
    boolean insertNewsType(NewsType newsType);

    /**
     * 修改新闻栏目
     *
     * @param newsType 新闻栏目
     * @return 结果
     */
    boolean updateNewsType(NewsType newsType);

    /**
     * 删除新闻栏目信息
     *
     * @param newsTypeIds 新闻栏目ID
     * @return 结果
     */
    boolean deleteNewsTypeByIds(List<Long> newsTypeIds);

    /**
     * 校验新闻栏目名称是否唯一
     *
     * @param newsType 新闻栏目
     * @return 结果
     */
    String checkNewsTypeNameUnique(NewsType newsType);
}

