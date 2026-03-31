package com.shisfish.news.service.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.common.constant.SymbolConstant;
import com.shisfish.news.common.constant.SysMenuConstants;
import com.shisfish.news.common.constant.UserConstants;
import com.shisfish.news.common.core.domain.TreeSelect;
import com.shisfish.news.dao.domain.system.SysMenu;
import com.shisfish.news.dao.domain.system.SysUser;
import com.shisfish.news.common.utils.SecurityUtils;
import com.shisfish.news.common.utils.string.StringUtils;
import com.shisfish.news.dao.domain.system.SysRoleMenu;
import com.shisfish.news.dao.viewobject.system.MetaVo;
import com.shisfish.news.dao.viewobject.system.RouterVo;
import com.shisfish.news.service.mapper.system.SysMenuMapper;
import com.shisfish.news.service.service.system.ISysMenuService;
import com.shisfish.news.service.service.system.ISysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    /**
     * 根据用户ID查询菜单权限
     *
     * @param userId 用户ID
     * @return 菜单权限列表
     */
    @Override
    public Set<String> findMenuPermsByUserId(Long userId) {
        List<String> perms = baseMapper.findMenuPermsByUserId(userId);
        Set<String> permSet = new HashSet<>();
        perms.stream()
                .filter(Objects::nonNull)
                .forEach(menu -> {
                    permSet.addAll(Arrays.asList(menu.trim().split(SymbolConstant.COMMA)));
                });
        return permSet;
    }

    /**
     * 根据父节点的ID获取所有子节点
     * 递归调用
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> toTree(List<SysMenu> list, Long parentId) {
        // 查询所有的pid的子类
        List<SysMenu> children = list.stream().filter(item -> item.getParentId().equals(parentId)).collect(Collectors.toList());
        // 查询所有的非pid的子类
        List<SysMenu> others = list.stream().filter(item -> !item.getParentId().equals(parentId)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(children)) {
            children.forEach(child -> {
                if (CollectionUtils.isNotEmpty(others)) {
                    toTree(others, child.getMenuId()).forEach(item -> child.getChildren().add(item));
                }
            });
        }
        return children;
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户名
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> findMenuTreeByUserId(Long userId) {
        List<SysMenu> menus = null;
        // 判断是否是管理员 管理员的查询出所有权限
        // 排除按钮 只查询目录和菜单
        if (SecurityUtils.isAdmin(userId)) {
            menus = baseMapper.findMenuTreeAll();
        } else {
            menus = baseMapper.findMenuTreeByUserId(userId);
        }
        // 调用 递归方式组成树状结构
        return toTree(menus, 0L);
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            // 是否隐藏
            router.setHidden("1".equals(menu.getVisible()))
                    // 路由名字
                    .setName(getRouteName(menu))
                    // 路由地址
                    .setPath(getRouterPath(menu))
                    // 组件路径
                    .setComponent(getComponent(menu))
                    // 其他元素
                    .setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()))
            ;
            // 设置子菜单
            List<SysMenu> cMenus = menu.getChildren();
            // C目录 M菜单 F按钮
            if (CollectionUtils.isNotEmpty(cMenus) && SysMenuConstants.Type.DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true)
                        .setRedirect("noRedirect")
                        // 递归调用
                        .setChildren(buildMenus(cMenus))
                ;
                // 是为菜单内部跳转 子菜单
            } else if (isMenuFrame(menu)) {
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                // 路由地址
                children.setPath(menu.getPath())
                        .setComponent(menu.getComponent())
                        // 路由名字 首字母大写
                        .setName(StringUtils.capitalize(menu.getPath()))
                        // 其他元素
                        .setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()))
                        .setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu   菜单信息
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> findMenuList(SysMenu menu, Long userId) {
        List<SysMenu> menuList = null;
        // 管理员显示所有菜单信息
        if (SysUser.isAdmin(userId)) {
            menuList = baseMapper.findMenuList(menu);
        } else {
            menu.getParams().put("userId", userId);
            menuList = baseMapper.findMenuListByUserId(menu);
        }

        return menuList;
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public SysMenu findMenuById(Long menuId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getMenuId, menuId)
        );
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus) {
        return toTree(menus, 0L).stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> findMenuListByUserId(Long userId) {
        return findMenuList(new SysMenu(), userId);
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Long> findMenuListByRoleId(Long roleId) {
        return baseMapper.findMenuListByRoleId(roleId);
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public String checkMenuNameUnique(SysMenu menu) {
        //空值判断 空 赋值 -1L
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        SysMenu info = baseMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        // 查询到的菜单id不一致 则唯一
        if (StringUtils.isNotNull(info) && !Objects.equals(info.getMenuId(), menuId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public boolean insertMenu(SysMenu menu) {
        return saveOrUpdate(menu);
    }

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public boolean updateMenu(SysMenu menu) {
        return saveOrUpdate(menu);
    }

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        return count(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, menuId)) > 0;
    }

    /**
     * 查询菜单是否存在角色
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkMenuExistRole(Long menuId) {
        return sysRoleMenuService.count(new LambdaQueryWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getMenuId, menuId)) > 0;
    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean deleteMenuById(Long menuId) {
        return removeById(menuId);
    }


    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        }
        return component;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenu menu) {
        // 首字母大写
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }


    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId()
                && SysMenuConstants.Type.DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame().toString())
        ) {
            routerPath = "/" + menu.getPath();
            // 非外链并且是一级目录（类型为菜单）
        } else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenu menu) {
        return menu.getParentId() == 0
                && SysMenuConstants.Type.MENU.equals(menu.getMenuType())
                && menu.getIsFrame().toString().equals(UserConstants.NO_FRAME);
    }
}
