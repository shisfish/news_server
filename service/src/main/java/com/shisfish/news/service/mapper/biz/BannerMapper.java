package com.shisfish.news.service.mapper.biz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.dao.domain.biz.Banner;
import org.apache.ibatis.annotations.Param;

/**
 * @author shisfish
 * @date 2023/8/23
 */
public interface BannerMapper extends BaseMapper<Banner>  {

    IPage<Banner> findPage(Page<Banner> bannerPage, @Param("banner") Banner banner);

}
