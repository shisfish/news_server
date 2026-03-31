package com.shisfish.news.service.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.dao.domain.system.SysLogininfo;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

public interface SysLogininfoMapper extends BaseMapper<SysLogininfo> {
    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    IPage<SysLogininfo> findPage(Page<SysLogininfo> sysLogininforPage, @Param("logininfo") SysLogininfo logininfo);

    /**
     * 清空系统登录日志
     *
     * @return 结果
     */
    void cleanLogininfor();

}