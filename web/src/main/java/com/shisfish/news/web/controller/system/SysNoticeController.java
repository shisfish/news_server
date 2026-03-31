package com.shisfish.news.web.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.common.core.controller.BaseController;
import com.shisfish.news.common.lang.annotation.Log;
import com.shisfish.news.common.lang.enums.BusinessType;
import com.shisfish.news.common.result.PageResult;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.common.utils.SecurityUtils;
import com.shisfish.news.dao.domain.system.SysNotice;
import com.shisfish.news.service.service.system.ISysNoticeService;
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
 * @Description: 公告 信息操作处理
 * @Version: 1.0.0
 */
@Api(tags = "公告通知管理")
@Slf4j
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController {

    @Autowired
    private ISysNoticeService sysNoticeService;

    /**
     * 获取通知公告列表
     */
    @ApiOperation(value = "条件获取通知公告列表", notes = "条件获取通知公告列表详情")
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list/{page}/{size}")
    public PageResult<List<SysNotice>> list(@PathVariable("page") int page, @PathVariable("size") int size, SysNotice notice) {
        log.debug("SysNotice查询条件->{}", notice);
        IPage<SysNotice> iPage = sysNoticeService.findPage(new Page<SysNotice>(page, size), notice);
        return ResultUtils.success(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), iPage.getRecords());
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @ApiOperation(value = "根据通知公告编号获取详细信息", notes = "根据通知公告编号获取详细信息详情")
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping("/{id}")
    public Result<SysNotice> getInfo(@PathVariable("id") Integer noticeId) {
        return ResultUtils.success(sysNoticeService.findNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @ApiOperation(value = "新增通知公告", notes = "新增通知公告详情")
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<String> add(@Validated @RequestBody SysNotice notice) {
        notice.setCreateBy(SecurityUtils.getUsername());
        return toResult(sysNoticeService.insertNotice(notice));
    }

    /**
     * 修改通知公告
     */
    @ApiOperation(value = "修改通知公告", notes = "修改通知公告详情")
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<String> edit(@Validated @RequestBody SysNotice notice) {
        notice.setUpdateBy(SecurityUtils.getUsername());
        return toResult(sysNoticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告
     */
    @ApiOperation(value = "删除通知公告", notes = "删除通知公告详情")
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public Result<String> remove(@PathVariable("noticeIds") List<Integer> noticeIds) {
        return toResult(sysNoticeService.deleteNoticeByIds(noticeIds));
    }
}
