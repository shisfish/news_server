package com.shisfish.news.web.controller.news;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.common.constant.Constants;
import com.shisfish.news.common.constant.UserConstants;
import com.shisfish.news.common.core.controller.BaseController;
import com.shisfish.news.common.lang.annotation.Log;
import com.shisfish.news.common.lang.enums.BusinessType;
import com.shisfish.news.common.request.NewsDeleteRequest;
import com.shisfish.news.common.request.NewsTypeChangeRequest;
import com.shisfish.news.common.result.PageResult;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.framework.security.service.TokenService;
import com.shisfish.news.dao.domain.biz.News;
import com.shisfish.news.service.service.biz.INewsService;
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
 * @Description: 新闻管理
 * @Version: 1.0.0
 */
@Api(tags = "新闻管理接口")
@Slf4j
@RestController
@RequestMapping("/news/news")
public class NewsController extends BaseController {

    @Autowired
    private INewsService newsService;

    @Autowired
    private TokenService tokenService;


    /**
     * 查询新闻列表
     */
    @ApiOperation(value = "条件分页查询新闻列表", notes = "条件分页查询新闻列表详情")
    @PreAuthorize("@ss.hasPermi('news:news:list')")
    @GetMapping("/list/{page}/{size}")
    public PageResult<List<News>> list(@PathVariable("page") int page, @PathVariable("size") int size, News news) {;
        log.debug("News查询条件->{}", news);
        IPage<News> iPage = newsService.findPageAll(new Page<>(page, size), news);
        return ResultUtils.success(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), iPage.getRecords());
    }


    /**
     * 获取新闻详细信息
     */
    @ApiOperation(value = "获取新闻详细信息", notes = "获取新闻详细信息详情")
    @PreAuthorize("@ss.hasPermi('news:news:query')")
    @GetMapping(value = "/{newsId}")
    public Result<News> getInfo(@PathVariable("newsId") Long newsId) {
        return ResultUtils.success(newsService.findNewsById(newsId));
    }

    /**
     * 新增新闻
     */
    @ApiOperation(value = "新增新闻", notes = "新增新闻详情")
    @PreAuthorize("@ss.hasPermi('news:news:add')")
    @Log(title = "新闻", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<String> add(@RequestBody News news) {
        return toResult(newsService.insertNews(news));
    }

    @ApiOperation(value = "新增新闻", notes = "新增新闻详情")
    @PreAuthorize("@ss.hasPermi('news:news:add')")
    @Log(title = "新闻", businessType = BusinessType.INSERT)
    @PutMapping("/v2/add")
    public Result<String> addV2(@RequestBody News news) {
        // 如果news.newsContent包含了 #RESOURCE# 则替换成 ../../resource/
        news.setNewsContent(news.getNewsContent().replace("#RESOURCE#", "../../resource/"));
        return toResult(newsService.insertNews(news));
    }

    /**
     * 修改新闻
     */
    @ApiOperation(value = "修改新闻", notes = "修改新闻详情")
    @PreAuthorize("@ss.hasPermi('news:news:edit')")
    @Log(title = "新闻", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<String> edit(@RequestBody News news) {
        return toResult(newsService.updateNews(news));
    }

    /**
     * 删除新闻
     */
    @ApiOperation(value = "删除新闻", notes = "删除新闻详情")
    @PreAuthorize("@ss.hasPermi('news:news:remove')")
    @Log(title = "新闻", businessType = BusinessType.DELETE)
    @DeleteMapping()
    public Result<String> remove(@RequestBody NewsDeleteRequest newsDeleteRequest) {
        return toResult(newsService.deleteNewsByIds(newsDeleteRequest.getNewsIds()));
    }


    /**
     * 公开私有修改
     */
    @ApiOperation(value = "公开私有新闻修改", notes = "公开私有新闻修改详情")
    @PreAuthorize("@ss.hasPermi('news:news:edit')")
    @Log(title = "新闻管理", businessType = BusinessType.UPDATE)
    @PutMapping("/change/isPublic")
    public Result<String> changeIsPublic(@RequestBody News news) {
        log.debug("公开私有修改News->{}", news);
        return toResult(newsService.changeIsPublic(news));
    }

    /**
     * 审核新闻修改
     */
    @ApiOperation(value = "审核新闻修改", notes = "审核新闻修改详情")
    @PreAuthorize("@ss.hasPermi('news:news:inspect')")
    @Log(title = "新闻管理", businessType = BusinessType.UPDATE)
    @PutMapping("/change/status")
    public Result<String> changeStatus(@RequestBody News news) {
        log.debug("审核新闻修改News->{}", news);
        return toResult(newsService.changeStatus(news));
    }

    /**
     * 获取所有新闻下拉列表
     */
    @ApiOperation(value = "获取所有新闻下拉列表", notes = "获取所有新闻下拉列表详情")
    @GetMapping("/list")
    public Result<List<News>> optionSelect() {
        return ResultUtils.success(newsService.list(new LambdaQueryWrapper<News>()
                .select(News::getNewsId, News::getNewsTitle)
                .eq(News::getDelFlag, UserConstants.NORMAL)
                .eq(News::getStatus, Constants.AuditFlag.AUDIT_PASSED)
        ));
    }

    /**
     * 批量更新新闻分类
     */
    @ApiOperation(value = "批量更新新闻分类", notes = "批量更新新闻分类")
    @PreAuthorize("@ss.hasPermi('news:news:edit')")
    @Log(title = "新闻管理", businessType = BusinessType.UPDATE)
    @PutMapping("/type/change")
    public Result<String> remove(@RequestBody NewsTypeChangeRequest newsTypeChangeRequest) {
        newsService.updateNewsTypeByNewsIds(newsTypeChangeRequest);
        return toResult(true);
    }

}
