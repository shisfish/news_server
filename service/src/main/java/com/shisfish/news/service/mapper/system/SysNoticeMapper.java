package com.shisfish.news.service.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.dao.domain.system.SysNotice;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

public interface SysNoticeMapper extends BaseMapper<SysNotice> {
    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    IPage<SysNotice> findPage(Page<SysNotice> page, @Param("notice") SysNotice notice);
}