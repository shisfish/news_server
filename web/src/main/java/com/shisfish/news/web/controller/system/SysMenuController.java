package com.shisfish.news.web.controller.system;

import com.shisfish.news.common.constant.Constants;
import com.shisfish.news.common.constant.UserConstants;
import com.shisfish.news.common.core.controller.BaseController;
import com.shisfish.news.common.core.domain.LoginUser;
import com.shisfish.news.common.core.domain.TreeSelect;
import com.shisfish.news.dao.domain.system.SysMenu;
import com.shisfish.news.common.lang.annotation.Log;
import com.shisfish.news.common.lang.enums.BusinessType;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.common.utils.ServletUtils;
import com.shisfish.news.common.utils.string.StringUtils;
import com.shisfish.news.framework.security.service.TokenService;
import com.shisfish.news.service.service.system.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 菜单信息
 * @Version: 1.0.0
 */
@Api(tags = "菜单权限管理")
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {

    @Autowired
    private ISysMenuService sysMenuService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取菜单列表
     */
    @ApiOperation(value = "获取菜单列表", notes = "获取菜单列表详情")
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public Result<List<SysMenu>> list(SysMenu menu) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        List<SysMenu> menus = sysMenuService.findMenuList(menu, userId);
        return ResultUtils.success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @ApiOperation(value = "根据菜单编号获取详细信息", notes = "根据菜单编号获取详细信息详情")
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    public Result<SysMenu> getInfo(@PathVariable("menuId") Long menuId) {
        return ResultUtils.success(sysMenuService.findMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/tree/select")
    public Result<List<TreeSelect>> treeselect(SysMenu menu) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        List<SysMenu> menus = sysMenuService.findMenuList(menu, userId);
        return ResultUtils.success(sysMenuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @ApiOperation(value = "加载对应角色菜单列表树", notes = "加载对应角色菜单列表树详情")
    @GetMapping(value = "/role/menu/tree/select/{roleId}")
    public Result<HashMap<String, Object>> roleMenuTreeSelect(@PathVariable("roleId") Long roleId) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        List<SysMenu> menus = sysMenuService.findMenuListByUserId(loginUser.getUser().getUserId());
        return ResultUtils.success(new HashMap<String, Object>() {{
            put("checkedKeys", sysMenuService.findMenuListByRoleId(roleId));
            put("menus", sysMenuService.buildMenuTreeSelect(menus));
        }});
    }

    /**
     * 新增菜单
     */
    @ApiOperation(value = "新增菜单", notes = "新增菜单详情")
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<String> add(@Validated @RequestBody SysMenu menu) {
        // 判断菜单名称是否重复
        if (UserConstants.NOT_UNIQUE.equals(sysMenuService.checkMenuNameUnique(menu))) {
            return ResultUtils.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame().toString())
                && !StringUtils.startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS)) {
            // 是否菜单外链
            return ResultUtils.error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        return toResult(sysMenuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @ApiOperation(value = "修改菜单", notes = "修改菜单详情")
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<String> edit(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(sysMenuService.checkMenuNameUnique(menu))) {
            return ResultUtils.error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame().toString())
                && !StringUtils.startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS)) {
            // 是否菜单外链
            return ResultUtils.error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        return toResult(sysMenuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     */
    @ApiOperation(value = "删除菜单", notes = "删除菜单详情")
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public Result<String> remove(@PathVariable("menuId") Long menuId) {
        if (sysMenuService.hasChildByMenuId(menuId)) {
            return ResultUtils.error("存在子菜单,不允许删除");
        }
        if (sysMenuService.checkMenuExistRole(menuId)) {
            return ResultUtils.error("菜单已分配,不允许删除");
        }
        return toResult(sysMenuService.deleteMenuById(menuId));
    }
}
