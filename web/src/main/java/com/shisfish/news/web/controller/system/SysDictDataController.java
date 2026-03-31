package com.shisfish.news.web.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.common.core.controller.BaseController;
import com.shisfish.news.dao.domain.system.SysDictData;
import com.shisfish.news.common.lang.annotation.Log;
import com.shisfish.news.common.lang.enums.BusinessType;
import com.shisfish.news.common.result.PageResult;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.service.service.system.ISysDictDataService;
import com.shisfish.news.service.service.system.ISysDictTypeService;
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
 * @Description: 数据字典信息
 * @Version: 1.0.0
 */
@Api(tags = "数据字典数据管理")
@Slf4j
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {

    @Autowired
    private ISysDictDataService sysDictDataService;

    @Autowired
    private ISysDictTypeService sysDictTypeService;

    /**
     * 条件分页获取字典列表
     *
     * @return 结果集合
     */
    @ApiOperation(value = "条件分页获取字典列表", notes = "条件分页获取字典列表详情")
    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list/{page}/{size}")
    public PageResult<List<SysDictData>> list(@PathVariable("page") int page, @PathVariable("size") int size, SysDictData dictData) {
        log.debug("SysDictData查询条件->{}", dictData);
        IPage<SysDictData> iPage = sysDictDataService.findPage(new Page<SysDictData>(page, size), dictData);
        return ResultUtils.success(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), iPage.getRecords());
    }

    /**
     * 查询字典数据详细信息
     */
    @ApiOperation(value = "查询字典数据详细信息", notes = "查询字典数据详细信息详情")
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictCode}")
    public Result<SysDictData> getInfo(@PathVariable("dictCode") Long dictCode) {
        return ResultUtils.success(sysDictDataService.findDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @ApiOperation(value = "根据字典类型查询字典数据信息", notes = "根据字典类型查询字典数据信息详情")
    @GetMapping(value = "/type/{dictType}")
    public Result<List<SysDictData>> dictType(@PathVariable String dictType) {
        return ResultUtils.success(sysDictTypeService.findDictDataByType(dictType));
    }

    /**
     * 新增字典类型
     */
    @ApiOperation(value = "新增字典类型", notes = "新增字典类型详情")
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<String> add(@Validated @RequestBody SysDictData dict) {
        return toResult(sysDictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典类型
     */
    @ApiOperation(value = "修改保存字典类型", notes = "修改保存字典类型详情")
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<String> edit(@Validated @RequestBody SysDictData dict) {
        return toResult(sysDictDataService.updateDictData(dict));
    }

    /**
     * 删除字典类型
     */
    @ApiOperation(value = "删除字典类型", notes = "删除字典类型详情")
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public Result<String> remove(@PathVariable List<Long> dictCodes) {
        return toResult(sysDictDataService.deleteDictDataByIds(dictCodes));
    }
}
