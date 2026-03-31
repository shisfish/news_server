package com.shisfish.news.web.controller.news;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.common.core.controller.BaseController;
import com.shisfish.news.common.lang.annotation.Log;
import com.shisfish.news.common.lang.enums.BusinessType;
import com.shisfish.news.common.result.PageResult;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.dao.domain.biz.NewsComment;
import com.shisfish.news.service.service.biz.INewsCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * @Description: 新闻评论管理
 * @Version: 1.0.0
 */
@Api(tags = "新闻评论管理接口")
@RestController
@RequestMapping("/news/comment")
public class NewsCommentController extends BaseController {

    @Autowired
    private INewsCommentService commentService;

    /**
     * 查询新闻评论列表
     */
    @ApiOperation(value = "条件分页查询新闻评论列表", notes = "条件分页查询新闻评论列表详情")
    @PreAuthorize("@ss.hasPermi('news:comment:list')")
    @GetMapping("/list/{page}/{size}")
    public PageResult<List<NewsComment>> list(@PathVariable("page") int page, @PathVariable("size") int size, NewsComment newsComment) {
        IPage<NewsComment> iPage = commentService.findPage(new Page<NewsComment>(page, size), newsComment);
        return ResultUtils.success(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), iPage.getRecords());
    }

    /**
     * 获取新闻评论详细信息
     */
    @ApiOperation(value = "获取新闻评论详细信息", notes = "获取新闻评论详细信息详情")
    @PreAuthorize("@ss.hasPermi('news:comment:query')")
    @GetMapping(value = "/{commentId}")
    public Result<NewsComment> getInfo(@PathVariable("commentId") Long commentId) {
        return ResultUtils.success(commentService.findNewsCommentById(commentId));
    }

    /**
     * 新增新闻评论
     */
    @ApiOperation(value = "新增新闻评论", notes = "新增新闻评论详情")
    @PreAuthorize("@ss.hasPermi('news:comment:add')")
    @Log(title = "新闻评论", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<String> add(@RequestBody NewsComment newsComment) {
        return toResult(commentService.insertNewsComment(newsComment));
    }

    /**
     * 修改新闻评论
     */
    @ApiOperation(value = "修改新闻评论", notes = "修改新闻评论详情")
    @PreAuthorize("@ss.hasPermi('news:comment:edit')")
    @Log(title = "新闻评论", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<String> edit(@RequestBody NewsComment newsComment) {
        return toResult(commentService.updateNewsComment(newsComment));
    }

    /**
     * 删除新闻评论
     */
    @PreAuthorize("@ss.hasPermi('news:comment:remove')")
    @Log(title = "新闻评论", businessType = BusinessType.DELETE)
    @DeleteMapping("/{commentIds}")
    public Result<String> remove(@PathVariable List<Long> commentIds) {
        return toResult(commentService.deleteNewsCommentByIds(commentIds));
    }
}
