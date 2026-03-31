package com.shisfish.news.web.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.common.constant.UserConstants;
import com.shisfish.news.common.core.controller.BaseController;
import com.shisfish.news.dao.domain.system.SysDictType;
import com.shisfish.news.common.lang.annotation.Log;
import com.shisfish.news.common.lang.enums.BusinessType;
import com.shisfish.news.common.result.PageResult;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.service.service.system.ISysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 数据字典信息
 * @Version: 1.0.0
 */
@Api(tags = "数据字典类型管理")
@Slf4j
@RestController
@RequestMapping("/system/dict/type")
public class SysDictTypeController extends BaseController {
    @Autowired
    private ISysDictTypeService dictTypeService;

    @ApiOperation(value = "条件分页获取字典类型列表", notes = "条件分页获取字典类型列表详情")
    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list/{page}/{size}")
    public PageResult<List<SysDictType>> list(@PathVariable("page") int page, @PathVariable("size") int size, SysDictType dictType) {
        log.debug("SysDictType查询条件->{}", dictType);
        IPage<SysDictType> iPage = dictTypeService.findPage(new Page<>(page, size), dictType);
        return ResultUtils.success(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), iPage.getRecords());
    }


    /**
     * 查询字典类型详细信息
     */
    @ApiOperation(value = "查询字典类型详细信息", notes = "查询字典类型详细信息详情")
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictId}")
    public Result<SysDictType> getInfo(@PathVariable("dictId") Long dictId) {
        return ResultUtils.success(dictTypeService.findDictTypeById(dictId));
    }


    /**
     * 新增字典类型
     */
    @ApiOperation(value = "新增字典类型", notes = "新增字典类型详情")
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<String> add(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return ResultUtils.error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        return toResult(dictTypeService.insertDictType(dict));
    }

    /**
     * 修改字典类型
     */
    @ApiOperation(value = "修改字典类型", notes = "修改字典类型详情")
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<String> edit(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return ResultUtils.error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        return toResult(dictTypeService.updateDictType(dict));
    }

    /**
     * 删除字典类型
     */
    @ApiOperation(value = "删除字典类型", notes = "删除字典类型详情")
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public Result<String> remove(@PathVariable("dictIds") List<Long> dictIds) {
        return toResult(dictTypeService.deleteDictTypeByIds(dictIds));
    }

    /**
     * 清空缓存
     */
    @ApiOperation(value = "清空缓存", notes = "清空缓存详情")
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clear/cache")
    public Result<Object> clearCache() {
        dictTypeService.clearCache();
        return ResultUtils.success();
    }

    /**
     * 获取字典选择框列表
     */
    @ApiOperation(value = "获取字典选择框列表", notes = "获取字典选择框列表详情")
    @GetMapping("/option/select")
    public Result<Object> optionselect() {
        List<SysDictType> dictTypes = dictTypeService.findDictTypeAll();
        return ResultUtils.success(dictTypes);
    }
}
