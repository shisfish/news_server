package com.shisfish.news.framework.security.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shisfish.news.common.constant.Constants;
import com.shisfish.news.common.core.domain.LoginUser;
import com.shisfish.news.dao.domain.system.SysUser;
import com.shisfish.news.common.utils.string.StringUtils;
import com.shisfish.news.service.service.system.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 用户验证处理: 自定义CustomerUserDetailsService实现UserDetailService, 并实现loadUserByUsername方法 ，
 * loadUserByUsername方法主要作用是查询用户是否存在和设置用户权限信息
 * @Version: 1.0.0
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private SysPermissionService sysPermissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询当前登录用户 判断是否存在
        // 如果存在就判断当前用户是否被删除
        // 如果存在并且没有被删除就判断当前用户是否被禁用
        SysUser user = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("用户名密码错误");
        }
        if (Constants.DeleteFlag.DELETED == user.getDelFlag()) {
            log.info("登录用户：{} 已被删除.", username);
            throw new UsernameNotFoundException("用户名密码错误");
        }
        if (Constants.AbleFlag.DISABLE == user.getStatus()) {
            log.info("登录用户：{} 已被停用.", username);
            throw new UsernameNotFoundException("用户名密码错误");
        }
        return createLoginUser(user);
    }

    /**
     * 创建UserDetails
     *
     * @param user 登录用户
     * @return UserDetails
     */
    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(sysPermissionService.getMenuPermission(user), user);
    }
}
