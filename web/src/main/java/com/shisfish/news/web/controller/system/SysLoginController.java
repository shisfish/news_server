package com.shisfish.news.web.controller.system;

import com.shisfish.news.common.core.domain.LoginBody;
import com.shisfish.news.common.core.domain.LoginUser;
import com.shisfish.news.dao.domain.system.SysMenu;
import com.shisfish.news.dao.domain.system.SysUser;
import com.shisfish.news.dao.domain.system.SysUserPasswordLog;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.common.result.code.ResponseCode;
import com.shisfish.news.common.utils.ServletUtils;
import com.shisfish.news.common.utils.spring.SpringUtils;
import com.shisfish.news.framework.security.service.SysLoginService;
import com.shisfish.news.framework.security.service.SysPermissionService;
import com.shisfish.news.framework.security.service.TokenService;
import com.shisfish.news.service.service.system.ISysUserPasswordLogService;
import com.shisfish.news.service.service.system.ISysMenuService;
import com.shisfish.news.service.service.system.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 登录验证
 * @Version: 1.0.0
 */
@Api(tags = "登录验证管理")
@RestController
public class
SysLoginController {

    @Autowired
    private SysLoginService sysLoginService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysPermissionService sysPermissionService;

    @Autowired
    private ISysMenuService sysMenuService;

    @Autowired
    private ISysUserPasswordLogService sysUserPasswordLogService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @ApiOperation(value = "用户认证登录", notes = "用户认证登录详情")
    @PostMapping("/login")
    public Result<HashMap<String, Object>> login(@RequestBody LoginBody loginBody) {
        // 生成令牌
        return ResultUtils.success(new HashMap<String, Object>() {{
            put("token", sysLoginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                    loginBody.getUuid()
            ));
        }});
    }

    /**
     * 获取登录用户信息
     *
     * @return 结果
     */
    @ApiOperation(value = "获取登录用户信息", notes = "获取登录用户信息详情")
    @GetMapping("/get/info")
    public Result<HashMap<String, Object>> getInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 存一下最后登录ip
        user.setLoginIp(loginUser.getIpaddr());
        SpringUtils.getBean(ISysUserService.class).saveOrUpdate(user);
        // 角色集合
        Set<String> roles = sysPermissionService.getRolePermission(user);
        // 菜单权限集合
        Set<String> permissions = sysPermissionService.getMenuPermission(user);
        return ResultUtils.success(new HashMap<String, Object>() {{
            put("user", user);
            put("roles", roles);
            put("permissions", permissions);
        }});
    }

    /**
     * 获取登录用户路由信息
     *
     * @return 结果
     */
    @ApiOperation(value = "获取登录用户路由信息", notes = "获取登录用户路由信息详情")
    @GetMapping("/get/routers")
    public Result<HashMap<String, Object>> getRouters() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        // 根据用户id查询拥有菜单列表
        List<SysMenu> menus = sysMenuService.findMenuTreeByUserId(user.getUserId());
        return ResultUtils.success(sysMenuService.buildMenus(menus));
    }

    @ApiOperation(value = "获取登录用户信息", notes = "获取登录用户信息详情")
    @PostMapping("/checkPassword")
    public Result checkPassword() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());

        // 获取最后一次修改密码时间
        SysUserPasswordLog sysUserPasswordLog = sysUserPasswordLogService.getLatestLog(loginUser.getUser().getUserId());
        if (sysUserPasswordLog == null) {
            return ResultUtils.error(ResponseCode.UPDATE_PASSWORD.getCode(), "请先修改初始密码");
        }

        Date latestModifyDate = sysUserPasswordLog.getCreateTime();
        Calendar instance = Calendar.getInstance();
        instance.setTime(latestModifyDate);

        // 管理员30天，普通用户90天（判断角色是否为管理员，roleKey包含admin即为管理员）
        Set<String> roles = sysPermissionService.getRolePermission(loginUser.getUser());
        boolean isAdmin = roles.stream().anyMatch(role -> "admin".equalsIgnoreCase(role.trim()));
        int validDays = isAdmin ? 30 : 90;
        instance.add(Calendar.DAY_OF_YEAR, validDays);

        Date now = new Date();
        if (now.before(instance.getTime())) {
            // 提前7天警告
            Calendar warn = Calendar.getInstance();
            warn.setTime(instance.getTime());
            warn.add(Calendar.DAY_OF_YEAR, -7);
            if (now.after(warn.getTime())) {
                return ResultUtils.error(ResponseCode.PASSWORD_WILL_EXPIRE.getCode(), "密码即将过期，请及时修改");
            }
            return ResultUtils.success();
        }
        return ResultUtils.error(ResponseCode.PASSWORD_EXPIRED.getCode(), "密码已过期，请立即修改");
    }
}
