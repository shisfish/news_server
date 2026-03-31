package com.shisfish.news.service.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.dao.domain.system.SysLogininfo;
import com.shisfish.news.service.mapper.system.SysLogininfoMapper;
import com.shisfish.news.service.service.system.ISysLogininfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

@Service
public class SysLogininfoServiceImpl extends ServiceImpl<SysLogininfoMapper, SysLogininfo> implements ISysLogininfoService {
    /**
     * 查询系统登录日志集合
     *
     * @param logininfo 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public IPage<SysLogininfo> findPage(Page<SysLogininfo> sysLogininforPage, SysLogininfo logininfo) {
        return baseMapper.findPage(sysLogininforPage, logininfo);
    }

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return
     */
    @Override
    public boolean deleteLogininforByIds(List<Long> infoIds) {
        return removeByIds(infoIds);
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor() {
        baseMapper.cleanLogininfor();
    }
}
