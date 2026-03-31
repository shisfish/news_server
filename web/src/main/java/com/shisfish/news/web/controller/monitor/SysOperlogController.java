package com.shisfish.news.web.controller.monitor;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.common.core.controller.BaseController;
import com.shisfish.news.common.lang.annotation.Log;
import com.shisfish.news.common.lang.enums.BusinessType;
import com.shisfish.news.common.result.PageResult;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.dao.domain.system.SysOperLog;
import com.shisfish.news.service.service.system.ISysOperLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 操作日志记录
 * @Version: 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController {

    @Autowired
    private ISysOperLogService operLogService;

    /**
     * 获取日志列表
     *
     * @return 结果集合
     */
    @PreAuthorize("@ss.hasPermi('monitor:operlog:list')")
    @GetMapping("/list/{page}/{size}")
    public PageResult<Object> list(@PathVariable("page") int page, @PathVariable("size") int size, SysOperLog operLog) {
        log.debug("SysOperLog查询条件->{}", operLog);
        IPage<SysOperLog> iPage = operLogService.findPage(new Page<SysOperLog>(page, size), operLog);
        return ResultUtils.success(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), iPage.getRecords());
    }

    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/{operIds}")
    public Result<String> remove(@PathVariable("operIds") List<Long> operIds) {
        return toResult(operLogService.deleteOperLogByIds(operIds));
    }

    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/clean")
    public Result<Object> clean() {
        operLogService.cleanOperLog();
        return ResultUtils.success();
    }
}
