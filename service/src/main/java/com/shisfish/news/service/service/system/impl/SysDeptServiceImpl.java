package com.shisfish.news.service.service.system.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.common.constant.Constants;
import com.shisfish.news.common.constant.SymbolConstant;
import com.shisfish.news.common.constant.UserConstants;
import com.shisfish.news.common.core.domain.TreeSelect;
import com.shisfish.news.dao.domain.system.SysDept;
import com.shisfish.news.common.exception.CustomException;
import com.shisfish.news.common.utils.string.StringUtils;
import com.shisfish.news.service.mapper.system.SysDeptMapper;
import com.shisfish.news.service.service.system.ISysDeptService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {
    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    public List<SysDept> findDeptList(SysDept dept) {
        return baseMapper.findDeptList(dept);
    }


    /**
     * 根据父节点的ID获取所有子节点
     * 递归调用
     *
     * @param list     列表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysDept> toTree(List<SysDept> list, Long parentId) {
        // 查询所有的pid的子类
        List<SysDept> children = list.stream().filter(item -> item.getParentId().equals(parentId)).collect(Collectors.toList());
        // 查询所有的非pid的子类
        List<SysDept> others = list.stream().filter(item -> !item.getParentId().equals(parentId)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(children)) {
            children.forEach(child -> {
                if (CollectionUtils.isNotEmpty(others)) {
                    toTree(others, child.getDeptId()).forEach(item -> child.getChildren().add(item));
                }
            });
        }
        return children;
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        return toTree(depts, 0L);
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts) {
        List<SysDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public SysDept findDeptById(Long deptId) {
        return getById(deptId);
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Long> findDeptListByRoleId(Long roleId) {
        return baseMapper.findDeptListByRoleId(roleId);
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public String checkDeptNameUnique(SysDept dept) {
        Long deptId = StringUtils.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
        SysDept info = baseMapper.selectOne(new LambdaQueryWrapper<SysDept>().eq(SysDept::getDeptName, dept.getDeptName()).eq(SysDept::getParentId, dept.getParentId()));
        if (StringUtils.isNotNull(info) && !Objects.equals(info.getDeptId(), deptId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public boolean insertDept(SysDept dept) {
        SysDept info = findDeptById(dept.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (Constants.AbleFlag.DISABLE == info.getStatus()) {
            throw new CustomException("部门停用，不允许新增");
        }
        dept.setAncestors(info.getAncestors() + SymbolConstant.COMMA + dept.getParentId());
        return saveOrUpdate(dept);
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public boolean findNormalChildrenDeptById(Long deptId) {
        return baseMapper.findNormalChildrenDeptById(deptId) > 0;
    }

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public boolean updateDept(SysDept dept) {
        // 新父节点
        SysDept newParentDept = getById(dept.getParentId());
        SysDept oldDept = getById(dept.getDeptId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + SymbolConstant.COMMA + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        boolean flag = saveOrUpdate(dept);
        if (Constants.AbleFlag.ENABLE == dept.getStatus()) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatus(dept);
        }
        return flag;
    }

    /**
     * 是否存在部门子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        return baseMapper.selectCount(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getDelFlag, Constants.DeleteFlag.NOT_DELETE)
                .eq(SysDept::getParentId, deptId)
        ) > 0;
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        return baseMapper.checkDeptExistUser(deptId) > 0;
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean deleteDeptById(Long deptId) {
        return removeById(deptId);
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatus(SysDept dept) {
        dept = getById(dept.getDeptId());
        baseMapper.updateDeptStatus(dept);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    private void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = baseMapper.findChildrenDeptById(deptId);
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            baseMapper.updateDeptChildren(children);
        }
    }

    @Override
    public List<SysDept> findDeptListWithParent(SysDept dept) {
        // 首先，获取所有符合条件的对象
        List<SysDept> deptList = findDeptList(dept);
        if (CollectionUtil.isEmpty(deptList)) {
            return deptList;
        }
        Map<Long, SysDept> resultMap = new HashMap<>();
        Map<Long, SysDept> deptMap = deptList.stream().collect(Collectors.toMap(SysDept::getDeptId, Function.identity()));

        // 先拷贝一份
        for (Long key : deptMap.keySet()) {
            SysDept sysDept = deptMap.get(key);
            resultMap.put(key, sysDept);
        }

        Iterator<Long> iterator = deptMap.keySet().iterator();
        while (iterator.hasNext()) {
            Long next = iterator.next();
            SysDept sysDept = deptMap.get(next);
            if (sysDept.getParentId() == 0L) {
                continue;
            }
            if (resultMap.containsKey(sysDept.getParentId())) {
                continue;
            }
            getAllParentDept(resultMap, sysDept);
        }
        return resultMap.values().stream().collect(Collectors.toList());
    }

    private void getAllParentDept(Map<Long, SysDept> resultMap, SysDept sysDept) {
        SysDept parentDept = findDeptById(sysDept.getParentId());
        if (parentDept == null) {
            return;
        }
        resultMap.put(parentDept.getDeptId(), parentDept);
        if (parentDept.getParentId() == 0L) {
            return;
        }
        if (resultMap.containsKey(parentDept.getParentId())) {
            return;
        }
        getAllParentDept(resultMap, parentDept);
    }

}

