package com.shisfish.news.web.controller.news;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.common.constant.UserConstants;
import com.shisfish.news.common.core.controller.BaseController;
import com.shisfish.news.common.lang.annotation.Log;
import com.shisfish.news.common.lang.enums.BusinessType;
import com.shisfish.news.common.result.PageResult;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.dao.domain.biz.NewsType;
import com.shisfish.news.service.service.biz.INewsTypeService;
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
 * @Description:
 * @Version: 1.0.0
 */
@Api(tags = "新闻栏目管理接口")
@Slf4j
@RestController
@RequestMapping("/news/type")
public class NewsTypeController extends BaseController {

    @Autowired
    private INewsTypeService newsTypeService;

    /**
     * 获取所有新闻栏目下拉列表
     */
    @ApiOperation(value = "获取所有新闻栏目下拉列表", notes = "获取所有新闻栏目下拉列表详情")
    @GetMapping("/list")
    public Result<List<NewsType>> optionSelect() {
        return ResultUtils.success(newsTypeService.list(new LambdaQueryWrapper<NewsType>()
                .select(NewsType::getNewsTypeId, NewsType::getNewsTypeName, NewsType::getStatus)
                .eq(NewsType::getDelFlag, UserConstants.NORMAL)
        ));
    }

    /**
     * 条件分页新闻栏目列表
     *
     * @return 结果集合
     */
    @ApiOperation(value = "条件分页新闻栏目列表", notes = "条件分页新闻栏目列表详情")
    @PreAuthorize("@ss.hasPermi('news:type:list')")
    @GetMapping("/list/{page}/{size}")
    public PageResult<List<NewsType>> list(@PathVariable("page") int page, @PathVariable("size") int size, NewsType newsType) {
        log.debug("NewsType查询条件->{}", newsType);
        IPage<NewsType> iPage = newsTypeService.findPage(new Page<NewsType>(page, size), newsType);
        return ResultUtils.success(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), iPage.getRecords());
    }

    /**
     * 获取新闻栏目详细信息
     */
    @ApiOperation(value = "获取新闻栏目详细信息", notes = "获取新闻栏目详细信息详情")
    @PreAuthorize("@ss.hasPermi('news:type:query')")
    @GetMapping(value = "/{newsTypeId}")
    public Result<NewsType> getInfo(@PathVariable("newsTypeId") Long newsTypeId) {
        return ResultUtils.success(newsTypeService.findNewsTypeById(newsTypeId));
    }

    /**
     * 新增新闻栏目
     */
    @ApiOperation(value = "新增新闻栏目", notes = "新增新闻栏目详情")
    @PreAuthorize("@ss.hasPermi('news:type:add')")
    @Log(title = "新闻栏目", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<String> add(@RequestBody NewsType newsType) {
        if (UserConstants.NOT_UNIQUE.equals(newsTypeService.checkNewsTypeNameUnique(newsType))) {
            return ResultUtils.error("新增新闻栏目'" + newsType.getNewsTypeName() + "'失败，新闻栏目名称已存在");
        }
        return toResult(newsTypeService.insertNewsType(newsType));
    }

    /**
     * 修改新闻栏目
     */
    @ApiOperation(value = "修改新闻栏目", notes = "修改新闻栏目详情")
    @PreAuthorize("@ss.hasPermi('news:type:edit')")
    @Log(title = "新闻栏目", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<String> edit(@RequestBody NewsType newsType) {
        if (UserConstants.NOT_UNIQUE.equals(newsTypeService.checkNewsTypeNameUnique(newsType))) {
            return ResultUtils.error("修改新闻栏目'" + newsType.getNewsTypeName() + "'失败，新闻栏目名称已存在");
        }
        return toResult(newsTypeService.updateNewsType(newsType));
    }

    /**
     * 删除新闻栏目
     */
    @ApiOperation(value = "删除新闻栏目", notes = "删除新闻栏目详情")
    @PreAuthorize("@ss.hasPermi('news:type:remove')")
    @Log(title = "新闻栏目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{newsTypeIds}")
    public Result<String> remove(@PathVariable List<Long> newsTypeIds) {
        return toResult(newsTypeService.deleteNewsTypeByIds(newsTypeIds));
    }
}
