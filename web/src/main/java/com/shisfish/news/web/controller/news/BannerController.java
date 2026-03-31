package com.shisfish.news.web.controller.news;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.common.core.controller.BaseController;
import com.shisfish.news.common.lang.annotation.Log;
import com.shisfish.news.common.lang.enums.BusinessType;
import com.shisfish.news.common.result.PageResult;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.dao.domain.biz.Banner;
import com.shisfish.news.service.service.biz.IBannerService;
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
 * @author shisfish
 * @date 2023/8/23
 */

@Api(tags = "Banner管理接口")
@Slf4j
@RestController
@RequestMapping("/news/banner")
public class BannerController extends BaseController {

    @Autowired
    private IBannerService bannerService;

    /**
     * 查询Banner列表
     */
    @ApiOperation(value = "条件分页查询Banner列表", notes = "条件分页查询Banner列表详情")
    @PreAuthorize("@ss.hasPermi('news:banner:list')")
    @GetMapping("/list/{page}/{size}")
    public PageResult<List<Banner>> list(@PathVariable("page") int page, @PathVariable("size") int size, Banner banner) {
        log.debug("Banner查询条件->{}", banner);
        IPage<Banner> iPage = bannerService.findPage(new Page<>(page, size), banner);
        return ResultUtils.success(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), iPage.getRecords());
    }


    /**
     * 获取Banner详细信息
     */
    @ApiOperation(value = "获取Banner详细信息", notes = "获取Banner详细信息详情")
    @PreAuthorize("@ss.hasPermi('news:banner:query')")
    @GetMapping(value = "/{id}")
    public Result<List<Banner>> getInfo(@PathVariable("id") Long id) {
        return ResultUtils.success(bannerService.findBannerById(id));
    }

    /**
     * 新增Banner
     */
    @ApiOperation(value = "新增Banner", notes = "新增Banner详情")
    @PreAuthorize("@ss.hasPermi('news:banner:add')")
    @Log(title = "Banner", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<String> add(@RequestBody Banner banner) {
        return toResult(bannerService.insertBanner(banner));
    }

    /**
     * 修改Banner
     */
    @ApiOperation(value = "修改Banner", notes = "修改Banner详情")
    @PreAuthorize("@ss.hasPermi('news:banner:edit')")
    @Log(title = "Banner", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<String> edit(@RequestBody Banner banner) {
        return toResult(bannerService.updateBanner(banner));
    }

    /**
     * 删除Banner
     */
    @ApiOperation(value = "删除Banner", notes = "删除Banner详情")
    @PreAuthorize("@ss.hasPermi('news:banner:remove')")
    @Log(title = "Banner", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result<String> remove(@PathVariable List<Long> ids) {
        return toResult(bannerService.deleteBannerByIds(ids));
    }

}
