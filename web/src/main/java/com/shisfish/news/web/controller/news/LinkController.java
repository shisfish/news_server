package com.shisfish.news.web.controller.news;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.common.core.controller.BaseController;
import com.shisfish.news.common.lang.annotation.Log;
import com.shisfish.news.common.lang.enums.BusinessType;
import com.shisfish.news.common.result.PageResult;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.dao.domain.biz.Link;
import com.shisfish.news.service.service.biz.ILinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * @Description: 友情链接
 * @Version: 1.0.0
 */
@Api(tags = "友情链接管理接口")
@Slf4j
@RestController
@RequestMapping("/news/link")
public class LinkController extends BaseController {

    @Autowired
    private ILinkService linkService;

    /**
     * 查询友情链接列表
     */
    @ApiOperation(value = "条件分页查询友情链接列表", notes = "条件分页查询友情链接列表详情")
    @PreAuthorize("@ss.hasPermi('news:link:list')")
    @GetMapping("/list/{page}/{size}")
    public PageResult<List<Link>> list(@PathVariable("page") int page, @PathVariable("size") int size, Link link) {
        log.debug("Link查询条件->{}", link);
        IPage<Link> iPage = linkService.findPage(new Page<Link>(page, size), link);
        return ResultUtils.success(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), iPage.getRecords());
    }


    /**
     * 获取友情链接详细信息
     */
    @ApiOperation(value = "获取友情链接详细信息", notes = "获取友情链接详细信息详情")
    @PreAuthorize("@ss.hasPermi('news:link:query')")
    @GetMapping(value = "/{id}")
    public Result<List<Link>> getInfo(@PathVariable("id") Long id) {
        return ResultUtils.success(linkService.findLinkById(id));
    }

    /**
     * 新增友情链接
     */
    @ApiOperation(value = "新增友情链接", notes = "新增友情链接详情")
    @PreAuthorize("@ss.hasPermi('news:link:add')")
    @Log(title = "友情链接", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<String> add(@RequestBody Link link) {
        return toResult(linkService.insertLink(link));
    }

    /**
     * 修改友情链接
     */
    @ApiOperation(value = "修改友情链接", notes = "修改友情链接详情")
    @PreAuthorize("@ss.hasPermi('news:link:edit')")
    @Log(title = "友情链接", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<String> edit(@RequestBody Link link) {
        return toResult(linkService.updateLink(link));
    }

    /**
     * 删除友情链接
     */
    @ApiOperation(value = "删除友情链接", notes = "删除友情链接详情")
    @PreAuthorize("@ss.hasPermi('news:link:remove')")
    @Log(title = "友情链接", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result<String> remove(@PathVariable List<Long> ids) {
        return toResult(linkService.deleteLinkByIds(ids));
    }
}
