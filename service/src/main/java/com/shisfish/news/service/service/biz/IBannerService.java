package com.shisfish.news.service.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shisfish.news.dao.domain.biz.Banner;

import java.util.List;

/**
 * @author shisfish
 * @date 2023/8/23
 */
public interface IBannerService extends IService<Banner> {


    /**
     * 查询友情链接列表
     *
     * @param banner 友情链接
     * @return 友情链接集合
     */
    IPage<Banner> findPage(Page<Banner> bannerPage, Banner banner);

    /**
     * 查询友情链接
     *
     * @param id 友情链接ID
     * @return 友情链接
     */
    Banner findBannerById(Long id);

    /**
     * 新增友情链接
     *
     * @param banner 友情链接
     * @return 结果
     */
    boolean insertBanner(Banner banner);

    /**
     * 修改友情链接
     *
     * @param banner 友情链接
     * @return 结果
     */
    boolean updateBanner(Banner banner);

    /**
     * 批量删除友情链接
     *
     * @param ids 需要删除的友情链接ID
     * @return 结果
     */
    boolean deleteBannerByIds(List<Long> ids);

}
