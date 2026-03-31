package com.shisfish.news.service.service.biz.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.dao.domain.biz.Banner;
import com.shisfish.news.service.mapper.biz.BannerMapper;
import com.shisfish.news.service.service.biz.IBannerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shisfish
 * @date 2023/8/23
 */

@Slf4j
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements IBannerService {

    @Autowired
    private BannerMapper bannerMapper;

    /**
     * 查询友情链接列表
     *
     * @param banner 友情链接
     * @return 友情链接集合
     */
    @Override
    public IPage<Banner> findPage(Page<Banner> bannerPage, Banner banner) {
        return baseMapper.findPage(bannerPage, banner);
    }

    /**
     * 查询友情链接
     *
     * @param id 友情链接ID
     * @return 友情链接
     */
    @Override
    public Banner findBannerById(Long id) {
        return getById(id);
    }

    /**
     * 新增友情链接
     *
     * @param banner 友情链接
     * @return 结果
     */
    @Override
    public boolean insertBanner(Banner banner) {
        return saveOrUpdate(banner);
    }

    /**
     * 修改友情链接
     *
     * @param banner 友情链接
     * @return 结果
     */
    @Override
    public boolean updateBanner(Banner banner) {
        return saveOrUpdate(banner);
    }

    /**
     * 批量删除友情链接
     *
     * @param ids 需要删除的友情链接ID
     * @return 结果
     */

    @Override
    public boolean deleteBannerByIds(List<Long> ids) {
        return removeByIds(ids);
    }

}
