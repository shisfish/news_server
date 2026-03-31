package com.shisfish.news.web.controller.monitor;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.common.core.controller.BaseController;
import com.shisfish.news.common.lang.annotation.Log;
import com.shisfish.news.common.lang.enums.BusinessType;
import com.shisfish.news.common.result.PageResult;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.dao.domain.system.SysLogininfo;
import com.shisfish.news.service.service.system.ISysLogininfoService;
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
 * @Description: 系统访问记录
 * @Version: 1.0.0
 */
@RestController
@Slf4j
@RequestMapping("/monitor/logininfo")
public class SysLogininfoController extends BaseController {
    @Autowired
    private ISysLogininfoService logininfoService;

    /**
     * 获取日志列表
     *
     * @return 结果集合
     */
    @PreAuthorize("@ss.hasPermi('monitor:logininfo:list')")
    @GetMapping("/list/{page}/{size}")
    public PageResult<Object> list(@PathVariable("page") int page, @PathVariable("size") int size, SysLogininfo logininfor) {
        log.info("SysLogininfor查询条件->{}", logininfor);
        IPage<SysLogininfo> iPage = logininfoService.findPage(new Page<>(page, size), logininfor);
        return ResultUtils.success(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), iPage.getRecords());
    }

    @PreAuthorize("@ss.hasPermi('monitor:logininfo:remove')")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public Result<Object> remove(@PathVariable("infoIds") List<Long> infoIds) {
        return toResult(logininfoService.deleteLogininforByIds(infoIds));
    }

    @PreAuthorize("@ss.hasPermi('monitor:logininfo:remove')")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public Result<Object> clean() {
        logininfoService.cleanLogininfor();
        return ResultUtils.success();
    }
}
