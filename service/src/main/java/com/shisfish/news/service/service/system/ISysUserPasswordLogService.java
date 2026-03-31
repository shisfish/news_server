package com.shisfish.news.service.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shisfish.news.dao.domain.system.SysUserPasswordLog;

/**
 * @author shisfish
 * @date 2023/8/29
 * @Description 用户修改密码记录
 */
public interface ISysUserPasswordLogService extends IService<SysUserPasswordLog> {
    SysUserPasswordLog getLatestLog(Long userId);
}
