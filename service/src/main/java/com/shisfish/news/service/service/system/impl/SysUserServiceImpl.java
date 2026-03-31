package com.shisfish.news.service.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.common.constant.SymbolConstant;
import com.shisfish.news.common.constant.UserConstants;
import com.shisfish.news.dao.domain.system.SysRole;
import com.shisfish.news.dao.domain.system.SysUser;
import com.shisfish.news.dao.domain.system.SysUserPasswordLog;
import com.shisfish.news.common.exception.CustomException;
import com.shisfish.news.common.utils.string.StringUtils;
import com.shisfish.news.dao.domain.system.SysUserRole;
import com.shisfish.news.service.mapper.system.SysRoleMapper;
import com.shisfish.news.service.mapper.system.SysUserMapper;
import com.shisfish.news.service.service.system.ISysUserPasswordLogService;
import com.shisfish.news.service.service.system.ISysUserRoleService;
import com.shisfish.news.service.service.system.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ISysUserPasswordLogService sysUserPasswordLogService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * 获取用户列表
     *
     * @param page 分页参数
     * @param user 条件查询对象，user
     * @return IPage<SysUser>
     */
    @Override
    public IPage<SysUser> findPage(Page<SysUser> page, SysUser user) {
        return baseMapper.findPage(page, user);
    }

    /**
     * 校验用户名是否唯一
     *
     * @param username 用户名
     * @return 结果
     */
    @Override
    public String checkUsernameUnique(String username) {
        // 查询该用户使用的人数 如果>0就已经使用过了
        int count = baseMapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        // 存在:1 不存在:0
        return (count > 0) ? UserConstants.NOT_UNIQUE : UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public String checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        // select user_id,phone from sys_user where phone = #{phone}
        SysUser info = baseMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .select(SysUser::getUserId, SysUser::getPhone)
                        .eq(SysUser::getPhone, user.getPhone()));
        // 存在:1 不存在:0 数据库里面用户id 不和 目前的userId一致
        if (StringUtils.isNotNull(info) && !Objects.equals(info.getUserId(), userId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public String checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        // select user_id,phone from sys_user where phone = #{phone}
        SysUser info = baseMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .select(SysUser::getUserId, SysUser::getEmail)
                .eq(SysUser::getEmail, user.getEmail()));
        // 存在:1 不存在:0  数据库里面用户id 不和 目前的userId一致
        if (StringUtils.isNotNull(info) && !Objects.equals(info.getUserId(), userId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertUser(SysUser user) {
        // 新增用户信息
        boolean flag = saveOrUpdate(user);
        // 新增用户与角色管理
        insertUserRole(user);

        // 新增用户密码变更表
        SysUserPasswordLog sysUserPasswordLog = new SysUserPasswordLog();
        sysUserPasswordLog.setUserId(user.getUserId());
        sysUserPasswordLog.setPassword(user.getPassword());
        sysUserPasswordLog.setCreateBy(user.getCreateBy());
        sysUserPasswordLog.setCreateTime(user.getCreateTime());
        sysUserPasswordLog.setType(UserConstants.PASSWORD_LOG_TYPE_ADD);
        sysUserPasswordLogService.save(sysUserPasswordLog);
        return flag;
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        List<Long> roleIds = user.getRoleIds();
        if (StringUtils.isNotNull(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<>();
            roleIds.forEach(id -> {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(id);
                list.add(ur);
            });
            if (list.size() > 0) {
                // 批量新增用户角色信息
                sysUserRoleService.saveOrUpdateBatch(list);
            }
        }

    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        // 判断是否是管理员 不允许操作超级管理员用户
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new CustomException("不允许操作超级管理员用户");
        }
    }

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUser(SysUser user) {
        Long userId = user.getUserId();
        // 删除用户与角色关联
        sysUserRoleService.remove(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId)
        );
        // 新增用户与角色管理
        insertUserRole(user);
        // 密码不能修改
        user.setPassword(null);
        return updateById(user);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    public boolean deleteUserByIds(List<Long> userIds) {
        // 循环判断 该用户是否是超级管理员 如果不是才可以进行 删除
        userIds.forEach(uid -> checkUserAllowed(new SysUser(uid)));
        // 批量逻辑删除
        return removeByIds(userIds);
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean resetPwd(SysUser user) {
        // 新增用户密码变更表
        SysUserPasswordLog sysUserPasswordLog = new SysUserPasswordLog();
        sysUserPasswordLog.setUserId(user.getUserId());
        sysUserPasswordLog.setPassword(user.getPassword());
        sysUserPasswordLog.setCreateBy(user.getUpdateBy());
        sysUserPasswordLog.setCreateTime(user.getUpdateTime());
        sysUserPasswordLog.setType(UserConstants.PASSWORD_LOG_TYPE_RESET);
        sysUserPasswordLogService.save(sysUserPasswordLog);
        return saveOrUpdate(user);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean updateUserStatus(SysUser user) {
        return saveOrUpdate(user);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser findByUserId(Long userId) {
        return baseMapper.findByUserId(userId);
    }

    /**
     * 根据用户ID查询用户所属角色组
     *
     * @param username 用户名
     * @return 结果
     */
    @Override
    public String findUserRoleGroup(String username) {
        List<SysRole> list = sysRoleMapper.findRolesByUsername(username);
        StringBuilder idString = new StringBuilder();
        // 拼接角色名称 admin,common,....
        for (SysRole role : list) {
            idString.append(role.getRoleName()).append(SymbolConstant.COMMA);
        }
        if (StringUtils.isNotEmpty(idString.toString())) {
            return idString.substring(0, idString.length() - 1);
        }
        return idString.toString();
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean updateUserProfile(SysUser user) {
        return saveOrUpdate(user);
    }

    /**
     * 重置用户密码
     *
     * @param username        用户名
     * @param encryptPassword 密码
     * @return 结果
     */
    @Override
    public boolean updateUserPwd(String username, String encryptPassword) {
        // 先查询 再更新
        SysUser user = baseMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
        );
        user.setPassword(encryptPassword);
        user.setUpdateBy(username);
        user.setUpdateTime(new Date());

        // 新增用户密码变更表
        SysUserPasswordLog sysUserPasswordLog = new SysUserPasswordLog();
        sysUserPasswordLog.setUserId(user.getUserId());
        sysUserPasswordLog.setPassword(user.getPassword());
        sysUserPasswordLog.setCreateBy(user.getUpdateBy());
        sysUserPasswordLog.setCreateTime(user.getUpdateTime());
        sysUserPasswordLog.setType(UserConstants.PASSWORD_LOG_TYPE_UPDATE);
        sysUserPasswordLogService.save(sysUserPasswordLog);

        return saveOrUpdate(user);
    }

    /**
     * 修改用户头像
     *
     * @param username 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String username, String avatar) {
        // 先查询 再更新
        SysUser user = baseMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
        );
        user.setAvatar(avatar);
        user.setUpdateTime(new Date());
        return saveOrUpdate(user);
    }
}
