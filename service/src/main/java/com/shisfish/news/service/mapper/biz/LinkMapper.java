package com.shisfish.news.service.mapper.biz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.dao.domain.biz.Link;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

public interface LinkMapper extends BaseMapper<Link> {
    /**
     * 查询友情链接列表
     *
     * @param link 友情链接
     *
     * @return 友情链接集合
     */
    IPage<Link> findPage(Page<Link> linkPage, @Param("link") Link link);
}