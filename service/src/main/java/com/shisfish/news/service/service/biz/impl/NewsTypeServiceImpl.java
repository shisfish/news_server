package com.shisfish.news.service.service.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.common.constant.UserConstants;
import com.shisfish.news.common.exception.CustomException;
import com.shisfish.news.common.utils.string.StringUtils;
import com.shisfish.news.dao.domain.biz.News;
import com.shisfish.news.dao.domain.biz.NewsType;
import com.shisfish.news.service.mapper.biz.NewsMapper;
import com.shisfish.news.service.mapper.biz.NewsTypeMapper;
import com.shisfish.news.service.service.biz.INewsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

@Service
public class NewsTypeServiceImpl extends ServiceImpl<NewsTypeMapper, NewsType> implements INewsTypeService {
    @Autowired
    private NewsMapper newsMapper;

    /**
     * 条件分页新闻栏目列表
     *
     * @param newsTypePage 分页插件
     * @param newsType     菜单栏目
     * @return 结果
     */
    @Override
    public IPage<NewsType> findPage(Page<NewsType> newsTypePage, NewsType newsType) {
        return baseMapper.findPage(newsTypePage, newsType);
    }

    /**
     * 查询新闻栏目
     *
     * @param newsTypeId 新闻栏目ID
     * @return 新闻栏目
     */
    @Override
    public NewsType findNewsTypeById(Long newsTypeId) {
        return getById(newsTypeId);
    }

    /**
     * 新增新闻栏目
     *
     * @param newsType 新闻栏目
     * @return 结果
     */
    @Override
    public boolean insertNewsType(NewsType newsType) {
        return saveOrUpdate(newsType);
    }

    /**
     * 修改新闻栏目
     *
     * @param newsType 新闻栏目
     * @return 结果
     */
    @Override
    public boolean updateNewsType(NewsType newsType) {
        return saveOrUpdate(newsType);
    }

    /**
     * 删除新闻栏目信息
     *
     * @param newsTypeIds 新闻栏目ID
     * @return 结果
     */
    @Override
    public boolean deleteNewsTypeByIds(List<Long> newsTypeIds) {
        // 判断新闻是否正在使用这个 新闻栏目
        newsTypeIds.stream().filter(Objects::nonNull)
                .forEach(newsTypeId -> {
                    // 根据id查询当前新闻栏目
                    NewsType newsType = getById(newsTypeId);
                    if (countNewsBynewsTypeId(newsTypeId)) {
                        throw new CustomException(String.format("%1$s已分配,不能删除", newsType.getNewsTypeName()));
                    }
                });
        return removeByIds(newsTypeIds);
    }


    /**
     * 通过新闻栏目ID查询新闻使用数量
     *
     * @param newsTypeId 新闻栏目id
     * @return 结果
     */
    private boolean countNewsBynewsTypeId(Long newsTypeId) {
        return newsMapper.selectCount(new LambdaQueryWrapper<News>().eq(News::getNewsTypeId, newsTypeId)) > 0;
    }


    /**
     * 校验新闻栏目名称是否唯一
     *
     * @param newsType 新闻栏目
     * @return 结果
     */
    @Override
    public String checkNewsTypeNameUnique(NewsType newsType) {
        Long newsTypeId = StringUtils.isNull(newsType.getNewsTypeId()) ? -1L : newsType.getNewsTypeId();
        // 根据新闻栏目名查询
        NewsType info = getOne(new LambdaQueryWrapper<NewsType>().eq(NewsType::getNewsTypeName, newsType.getNewsTypeName()));
        if (StringUtils.isNotNull(info) && !Objects.equals(info.getNewsTypeId(), newsTypeId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}

