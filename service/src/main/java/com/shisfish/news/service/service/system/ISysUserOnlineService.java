package com.shisfish.news.service.service.system;

import com.shisfish.news.common.core.domain.LoginUser;
import com.shisfish.news.dao.domain.system.SysUserOnline;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */
public interface ISysUserOnlineService {

    /**
     * 通过登录地址查询信息
     *
     * @param ipaddr 登录地址
     * @param user   用户信息
     * @return 在线用户信息
     */
    SysUserOnline findOnlineByIpaddr(String ipaddr, LoginUser user);


    /**
     * 通过用户名查询信息
     *
     * @param userName 用户名
     * @param user     用户信息
     * @return 在线用户信息
     */
    SysUserOnline findOnlineByUserName(String userName, LoginUser user);

    /**
     * 通过登录地址/用户名查询信息
     *
     * @param ipaddr   登录地址
     * @param userName 用户名
     * @param user     用户信息
     * @return 在线用户信息
     */
    SysUserOnline findOnlineByInfo(String ipaddr, String userName, LoginUser user);

    /**
     * 设置在线用户信息
     *
     * @param user 用户信息
     * @return 在线用户
     */
    SysUserOnline setLoginUserToUserOnline(LoginUser user);
}
