package com.shisfish.news.service.service.system.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.dao.domain.system.SysUserPasswordLog;
import com.shisfish.news.service.mapper.system.SysUserPasswordLogMapper;
import com.shisfish.news.service.service.system.ISysUserPasswordLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shisfish
 * @date 2023/8/29
 * @Description 用户变更密码记录
 */
@Service
public class SysUserPasswordLogServiceImpl extends ServiceImpl<SysUserPasswordLogMapper, SysUserPasswordLog> implements ISysUserPasswordLogService {

    @Autowired
    private SysUserPasswordLogMapper sysUserPasswordLogMapper;

    @Override
    public SysUserPasswordLog getLatestLog(Long userId) {
        return sysUserPasswordLogMapper.selectOne(Wrappers.<SysUserPasswordLog>lambdaQuery()
                .eq(SysUserPasswordLog::getUserId, userId)
                .orderByDesc(SysUserPasswordLog::getCreateTime)
                .last(" limit 1"));
    }
}
