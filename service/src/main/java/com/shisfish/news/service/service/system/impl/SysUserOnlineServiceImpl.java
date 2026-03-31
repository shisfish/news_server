package com.shisfish.news.service.service.system.impl;

import com.shisfish.news.common.core.domain.LoginUser;
import com.shisfish.news.dao.domain.system.SysDept;
import com.shisfish.news.common.utils.spring.SpringUtils;
import com.shisfish.news.common.utils.string.StringUtils;
import com.shisfish.news.dao.domain.system.SysUserOnline;
import com.shisfish.news.service.service.system.ISysDeptService;
import com.shisfish.news.service.service.system.ISysUserOnlineService;
import org.springframework.stereotype.Service;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */
@Service
public class SysUserOnlineServiceImpl implements ISysUserOnlineService {

    /**
     * 通过登录地址查询信息
     *
     * @param ipaddr 登录地址
     * @param user   用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline findOnlineByIpaddr(String ipaddr, LoginUser user) {
        if (StringUtils.equals(ipaddr, user.getIpaddr())) {
            return setLoginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过用户名查询信息
     *
     * @param userName 用户名
     * @param user     用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline findOnlineByUserName(String userName, LoginUser user) {
        if (StringUtils.equals(userName, user.getUsername())) {
            return setLoginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过登录地址/用户名查询信息
     *
     * @param ipaddr   登录地址
     * @param userName 用户名
     * @param user     用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline findOnlineByInfo(String ipaddr, String userName, LoginUser user) {
        if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername())) {
            return setLoginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 设置在线用户信息
     *
     * @param user 用户信息
     * @return 在线用户
     */
    @Override
    public SysUserOnline setLoginUserToUserOnline(LoginUser user) {
        if (StringUtils.isNull(user) && StringUtils.isNull(user.getUser())) {
            return null;
        }
        SysUserOnline sysUserOnline = new SysUserOnline();
        sysUserOnline
                .setTokenId(user.getToken())
                .setUsername(user.getUsername())
                .setIpaddr(user.getIpaddr())
                .setLoginLocation(user.getLoginLocation())
                .setBrowser(user.getBrowser())
                .setOs(user.getOs())
                .setLoginTime(user.getLoginTime());
        if (StringUtils.isNotNull(user.getUser().getDeptId())) {
            SysDept dept = SpringUtils.getBean(ISysDeptService.class).findDeptById(user.getUser().getDeptId());
            sysUserOnline.setDeptName(dept.getDeptName());
        }
        return sysUserOnline;
    }

}
