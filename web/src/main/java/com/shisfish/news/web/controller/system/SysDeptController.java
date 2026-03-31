package com.shisfish.news.web.controller.system;

import com.shisfish.news.common.constant.Constants;
import com.shisfish.news.common.constant.SymbolConstant;
import com.shisfish.news.common.constant.UserConstants;
import com.shisfish.news.common.core.controller.BaseController;
import com.shisfish.news.common.core.domain.TreeSelect;
import com.shisfish.news.dao.domain.system.SysDept;
import com.shisfish.news.common.lang.annotation.Log;
import com.shisfish.news.common.lang.enums.BusinessType;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.common.utils.string.StringUtils;
import com.shisfish.news.service.service.system.ISysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
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
 * @Description:
 * @Version: 1.0.0
 */
@Api(tags = "部门管理接口")
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {

    @Autowired
    private ISysDeptService sysDeptService;

    @ApiOperation(value = "获取部门列表", notes = "获取部门列表详情")
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public Result<List<SysDept>> list(SysDept dept) {
        List<SysDept> depts = sysDeptService.findDeptListWithParent(dept);
        return ResultUtils.success(depts);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @ApiOperation(value = "查询部门列表(排除某个子节点)", notes = "查询部门列表(排除某个子节点)详情")
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    public Result<List<SysDept>> excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDept> depts = sysDeptService.findDeptList(new SysDept());
        depts.removeIf(d -> d.getDeptId().intValue() == deptId || ArrayUtils.contains(StringUtils.split(d.getAncestors(), SymbolConstant.COMMA), deptId + ""));
        return ResultUtils.success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @ApiOperation(value = "根据部门编号获取详细信息", notes = "根据部门编号获取详细信息详情")
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public Result<SysDept> getInfo(@PathVariable("deptId") Long deptId) {
        return ResultUtils.success(sysDeptService.findDeptById(deptId));
    }

    /**
     * 获取部门下拉树列表
     */
    @ApiOperation(value = "获取部门下拉树列表", notes = "获取部门下拉树列表详情")
    @GetMapping("/tree/select")
    public Result<List<TreeSelect>> treeSelect(SysDept dept) {
        List<SysDept> depts = sysDeptService.findDeptList(dept);
        // 构造下拉列表树
        return ResultUtils.success(sysDeptService.buildDeptTreeSelect(depts));
    }


    /**
     * 加载对应角色部门列表树
     */
    @ApiOperation(value = "加载对应角色部门列表树", notes = "加载对应角色部门列表树详情")
    @GetMapping(value = "/role/dept/tree/select/{id}")
    public Result<HashMap<String, Object>> roleDeptTreeselect(@PathVariable("id") Long roleId) {
        List<SysDept> depts = sysDeptService.findDeptList(new SysDept());
        return ResultUtils.success(new HashMap<String, Object>() {{
            put("checkedKeys", sysDeptService.findDeptListByRoleId(roleId));
            put("depts", sysDeptService.buildDeptTreeSelect(depts));
        }});
    }

    /**
     * 新增部门
     */
    @ApiOperation(value = "新增部门", notes = "新增部门详情")
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<String> add(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(sysDeptService.checkDeptNameUnique(dept))) {
            return ResultUtils.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        return toResult(sysDeptService.insertDept(dept));
    }


    /**
     * 修改部门
     */
    @ApiOperation(value = "修改部门", notes = "修改部门详情")
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<String> edit(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(sysDeptService.checkDeptNameUnique(dept))) {
            return ResultUtils.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(dept.getDeptId())) {
            return ResultUtils.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (Constants.AbleFlag.DISABLE == dept.getStatus() && sysDeptService.findNormalChildrenDeptById(dept.getDeptId())) {
            return ResultUtils.error("该部门包含未停用的子部门！");
        }
        return toResult(sysDeptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @ApiOperation(value = "删除部门", notes = "删除部门详情")
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public Result<String> remove(@PathVariable Long deptId) {
        if (sysDeptService.hasChildByDeptId(deptId)) {
            return ResultUtils.error("存在下级部门,不允许删除");
        }
        if (sysDeptService.checkDeptExistUser(deptId)) {
            return ResultUtils.error("部门存在用户,不允许删除");
        }
        return toResult(sysDeptService.deleteDeptById(deptId));
    }
}
