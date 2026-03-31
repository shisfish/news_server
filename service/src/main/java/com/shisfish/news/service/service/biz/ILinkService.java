package com.shisfish.news.service.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shisfish.news.dao.domain.biz.Link;

import java.util.List;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

public interface ILinkService extends IService<Link> {

    /**
     * 查询友情链接列表
     *
     * @param link 友情链接
     * @return 友情链接集合
     */
    IPage<Link> findPage(Page<Link> linkPage, Link link);

    /**
     * 查询友情链接
     *
     * @param id 友情链接ID
     * @return 友情链接
     */
    Link findLinkById(Long id);

    /**
     * 新增友情链接
     *
     * @param link 友情链接
     * @return 结果
     */
    boolean insertLink(Link link);

    /**
     * 修改友情链接
     *
     * @param link 友情链接
     * @return 结果
     */
    boolean updateLink(Link link);

    /**
     * 批量删除友情链接
     *
     * @param ids 需要删除的友情链接ID
     * @return 结果
     */
    boolean deleteLinkByIds(List<Long> ids);
}
