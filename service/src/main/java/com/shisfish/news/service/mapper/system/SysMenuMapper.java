package com.shisfish.news.service.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shisfish.news.dao.domain.system.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 根据用户ID查询菜单权限
     *
     * @param userId 用户ID
     * @return 菜单权限列表
     */
    List<String> findMenuPermsByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    List<SysMenu> findMenuTreeAll();

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> findMenuTreeByUserId(@Param("userId") Long userId);

    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    List<SysMenu> findMenuList(@Param("menu") SysMenu menu);

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    List<SysMenu> findMenuListByUserId(@Param("menu") SysMenu menu);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    List<Long> findMenuListByRoleId(@Param("roleId") Long roleId);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @return 结果
     */
    SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);
}