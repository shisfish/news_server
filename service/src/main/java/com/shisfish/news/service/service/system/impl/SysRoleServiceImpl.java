package com.shisfish.news.service.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.common.constant.Constants;
import com.shisfish.news.common.constant.SymbolConstant;
import com.shisfish.news.common.constant.UserConstants;
import com.shisfish.news.dao.domain.system.SysRole;
import com.shisfish.news.common.exception.CustomException;
import com.shisfish.news.common.utils.string.StringUtils;
import com.shisfish.news.dao.domain.system.SysRoleDept;
import com.shisfish.news.dao.domain.system.SysRoleMenu;
import com.shisfish.news.dao.domain.system.SysUserRole;
import com.shisfish.news.service.mapper.system.SysRoleMapper;
import com.shisfish.news.service.service.system.ISysRoleDeptService;
import com.shisfish.news.service.service.system.ISysRoleMenuService;
import com.shisfish.news.service.service.system.ISysRoleService;
import com.shisfish.news.service.service.system.ISysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private ISysRoleDeptService sysRoleDeptService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ISysRoleMenuService iSysRoleMenuService;

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    public IPage<SysRole> findPage(Page<SysRole> sysRolePage, SysRole role) {
        return baseMapper.findPage(sysRolePage, role);
    }

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> findRolePermsByUserId(Long userId) {
        // 根据用户查询角色列表
        List<SysRole> roles = baseMapper.findRolePermsByUserId(userId);
        Set<String> roleSet = new HashSet<>();
        // role字符串标识 拼接 admin,common
        roles.stream()
                .filter(Objects::nonNull)
                .forEach(role -> {
                    roleSet.addAll(Arrays.asList(role.getRoleKey().trim().split(SymbolConstant.COMMA)));
                });
        return roleSet;
    }


    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<SysRole> findRoleAll() {
        return list(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getDelFlag, Constants.DeleteFlag.NOT_DELETE)
                .orderByAsc(SysRole::getRoleSort)
        );
    }

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<Long> findRoleListByUserId(Long userId) {
        return baseMapper.findRoleListByUserId(userId);
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public SysRole findRoleById(Long roleId) {
        return baseMapper.selectById(roleId);
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleNameUnique(SysRole role) {
        // roleId判断是否为空
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        // 根据角色名查询
        SysRole info = baseMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleName, role.getRoleName()));
        if (StringUtils.isNotNull(info) && !Objects.equals(info.getRoleId(), roleId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleKeyUnique(SysRole role) {
        // roleId判断是否为空
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = baseMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleKey, role.getRoleKey()));
        if (StringUtils.isNotNull(info) && !Objects.equals(info.getRoleId(), roleId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin()) {
            throw new CustomException("不允许操作超级管理员角色");
        }
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertRole(SysRole role) {
        // 新增角色信息
        saveOrUpdate(role);
        // 新增中间表信息
        return insertRoleMenu(role);
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    private boolean insertRoleMenu(SysRole role) {
        if (CollectionUtils.isNotEmpty(role.getMenuIds())) {
            List<SysRoleMenu> list = new ArrayList<>();
            role.getMenuIds().stream().filter(Objects::nonNull).forEach(menuId -> {
                SysRoleMenu rm = new SysRoleMenu();
                rm.setRoleId(role.getRoleId());
                rm.setMenuId(menuId);
                list.add(rm);
            });
            return list.size() > 0 && iSysRoleMenuService.saveOrUpdateBatch(list);
        }
        return true;
    }


    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRole(SysRole role) {
        // 修改角色信息
        saveOrUpdate(role);
        // 删除角色与菜单关联
        iSysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, role.getRoleId()));
        // 新增关联信息
        return insertRoleMenu(role);
    }

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean authDataScope(SysRole role) {
        // 修改角色信息
        saveOrUpdate(role);
        // 删除角色与部门关联
        sysRoleDeptService.remove(new LambdaQueryWrapper<SysRoleDept>().eq(SysRoleDept::getRoleId, role.getRoleId()));
        // 新增角色和部门信息（数据权限）
        return insertRoleDept(role);
    }

    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean updateRoleStatus(SysRole role) {
        return saveOrUpdate(role);
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    public boolean deleteRoleByIds(List<Long> roleIds) {
        roleIds.stream().filter(Objects::nonNull).forEach(roleId -> {
            checkRoleAllowed(new SysRole(roleId));
            SysRole role = findRoleById(roleId);
            if (countUserRoleByRoleId(roleId)) {
                throw new CustomException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        });
        return removeByIds(roleIds);
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    private boolean countUserRoleByRoleId(Long roleId) {
        return sysUserRoleService.count(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, roleId)) > 0;
    }

    /**
     * 新增角色部门信息(数据权限)
     *
     * @param role 角色对象
     */
    private boolean insertRoleDept(SysRole role) {
        if (CollectionUtils.isNotEmpty(role.getDeptIds())) {
            // 新增角色与部门（数据权限）管理
            List<SysRoleDept> list = new ArrayList<SysRoleDept>();
            role.getDeptIds().forEach(deptId -> {
                SysRoleDept rd = new SysRoleDept();
                rd.setDeptId(deptId);
                rd.setRoleId(role.getRoleId());
                list.add(rd);
            });
            return list.size() > 0 && sysRoleDeptService.saveOrUpdateBatch(list);
        }
        return true;
    }
}
