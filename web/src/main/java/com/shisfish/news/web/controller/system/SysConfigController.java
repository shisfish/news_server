package com.shisfish.news.web.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.common.constant.UserConstants;
import com.shisfish.news.common.core.controller.BaseController;
import com.shisfish.news.common.lang.annotation.Log;
import com.shisfish.news.common.lang.annotation.RepeatSubmit;
import com.shisfish.news.common.lang.enums.BusinessType;
import com.shisfish.news.common.result.PageResult;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.dao.domain.system.SysConfig;
import com.shisfish.news.service.service.system.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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

import java.util.List;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 参数配置 信息操作处理
 * @Version: 1.0.0
 */
@Api(tags = "参数配置管理接口")
@Slf4j
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {

    @Autowired
    private ISysConfigService configService;

    @ApiOperation(value = "条件分页获取参数配置信息列表", notes = "条件分页获取参数配置信息列表详情")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list/{page}/{size}")
    public PageResult<List<SysConfig>> list(@PathVariable("page") int page, @PathVariable("size") int size, SysConfig config) {
        log.debug("SysConfig查询条件->{}", config);
        IPage<SysConfig> iPage = configService.findPage(new Page<>(page, size), config);
        return ResultUtils.success(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), iPage.getRecords());
    }

    /**
     * 根据参数编号获取详细信息
     */
    @ApiOperation(value = "根据参数编号获取详细信息", notes = "根据参数编号获取详细信息详情")
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping("/{id}")
    public Result<SysConfig> getInfo(@PathVariable("id") Integer configId) {
        return ResultUtils.success(configService.findConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @ApiOperation(value = "根据参数键名查询参数", notes = "根据参数键名查询参数值详情")
    @GetMapping(value = "/configKey/{configKey}")
    public Result<String> getConfigKey(@PathVariable String configKey) {
        return ResultUtils.success(configService.findConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @ApiOperation(value = "新增参数配置", notes = "新增参数配置详情")
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public Result<String> add(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return ResultUtils.error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        return toResult(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @ApiOperation(value = "修改参数配置", notes = "修改参数配置详情")
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<String> edit(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return ResultUtils.error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        return toResult(configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @ApiOperation(value = "删除参数配置", notes = "删除参数配置详情")
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public Result<String> remove(@PathVariable List<Integer> configIds) {
        return toResult(configService.deleteConfigByIds(configIds));
    }

    /**
     * 清空缓存
     */
    @ApiOperation(value = "清空缓存", notes = "清空缓存详情")
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clear/cache")
    public Result<Object> clearCache() {
        configService.clearCache();
        return ResultUtils.success();
    }

}
