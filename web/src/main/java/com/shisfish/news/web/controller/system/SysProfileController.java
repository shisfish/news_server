package com.shisfish.news.web.controller.system;

import com.shisfish.news.common.config.NewsConfig;
import com.shisfish.news.common.core.domain.LoginUser;
import com.shisfish.news.dao.domain.system.SysUser;
import com.shisfish.news.common.lang.annotation.Log;
import com.shisfish.news.common.lang.enums.BusinessType;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.common.utils.SecurityUtils;
import com.shisfish.news.common.utils.ServletUtils;
import com.shisfish.news.common.utils.file.FileUploadUtils;
import com.shisfish.news.framework.security.service.TokenService;
import com.shisfish.news.service.service.system.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 个人信息 业务处理
 * @Version: 1.0.0
 */
@Api(tags = "个人信息管理")
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取个人用户信息
     */
    @ApiOperation(value = "获取个人用户信息", notes = "获取个人用户信息详情")
    @GetMapping
    public Result<HashMap<String, Object>> profile() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        return ResultUtils.success(new HashMap<String, Object>() {
            {
                put("roleGroup", sysUserService.findUserRoleGroup(user.getUsername()));
                put("userInfo", sysUserService.findByUserId(user.getUserId()));
            }
        });
    }


    /**
     * 修改用户
     */
    @ApiOperation(value = "修改用户", notes = "修改用户详情")
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<String> updateProfile(@RequestBody SysUser user) {
        if (sysUserService.updateUserProfile(user)) {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            // 更新缓存用户信息
            loginUser.getUser().setNickName(user.getNickName());
            loginUser.getUser().setPhone(user.getPhone());
            loginUser.getUser().setEmail(user.getEmail());
            loginUser.getUser().setSex(user.getSex());
            tokenService.setLoginUser(loginUser);
            return ResultUtils.success();
        }
        return ResultUtils.error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @ApiOperation(value = "重置密码", notes = "重置密码详情")
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/update/pwd")
    public Result<String> updatePwd(String oldPassword, String newPassword) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return ResultUtils.error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return ResultUtils.error("新密码不能与旧密码相同");
        }
        if (sysUserService.updateUserPwd(username, SecurityUtils.encryptPassword(newPassword))) {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);
            return ResultUtils.success();
        }
        return ResultUtils.error("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @ApiOperation(value = "头像上传", notes = "头像上传详情")
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public Result<HashMap<String, Object>> avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResultUtils.error("请选择文件上传");
        }
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String avatar = FileUploadUtils.upload(NewsConfig.getAvatarPath(), file);
        if (sysUserService.updateUserAvatar(loginUser.getUsername(), avatar)) {
            // 更新缓存用户头像
            loginUser.getUser().setAvatar(avatar);
            tokenService.setLoginUser(loginUser);
            return ResultUtils.success(new HashMap<String, Object>() {{
                put("imgUrl", avatar);
            }});
        }
        return ResultUtils.error("上传图片异常，请联系管理员");
    }
}
