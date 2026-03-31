package com.shisfish.news.service.service.biz.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.dao.domain.biz.Link;
import com.shisfish.news.service.mapper.biz.LinkMapper;
import com.shisfish.news.service.service.biz.ILinkService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements ILinkService {
    /**
     * 查询友情链接列表
     *
     * @param link 友情链接
     * @return 友情链接集合
     */
    @Override
    public IPage<Link> findPage(Page<Link> linkPage, Link link) {
        return baseMapper.findPage(linkPage, link);
    }

    /**
     * 查询友情链接
     *
     * @param id 友情链接ID
     * @return 友情链接
     */
    @Override
    public Link findLinkById(Long id) {
        return getById(id);
    }

    /**
     * 新增友情链接
     *
     * @param link 友情链接
     * @return 结果
     */
    @Override
    public boolean insertLink(Link link) {
        return saveOrUpdate(link);
    }

    /**
     * 修改友情链接
     *
     * @param link 友情链接
     * @return 结果
     */
    @Override
    public boolean updateLink(Link link) {
        return saveOrUpdate(link);
    }

    /**
     * 批量删除友情链接
     *
     * @param ids 需要删除的友情链接ID
     * @return 结果
     */

    @Override
    public boolean deleteLinkByIds(List<Long> ids) {
        return removeByIds(ids);
    }
}
