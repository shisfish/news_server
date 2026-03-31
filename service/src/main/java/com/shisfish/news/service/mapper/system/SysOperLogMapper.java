package com.shisfish.news.service.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.dao.domain.system.SysOperLog;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

public interface SysOperLogMapper extends BaseMapper<SysOperLog> {
    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    IPage<SysOperLog> findPage(Page<SysOperLog> sysOperLogPage, @Param("operLog") SysOperLog operLog);

    /**
     * 清空操作日志
     */
    void cleanOperLog();

}