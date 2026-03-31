package com.shisfish.news.web.controller.news;

import com.shisfish.news.common.core.controller.BaseController;
import com.shisfish.news.common.result.PageResult;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.dao.domain.biz.NewsDocument;
import com.shisfish.news.service.service.biz.INewsDocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 全局检索管理
 * @Version: 1.0.0
 */
@Api(tags = "新闻全局检索管理接口")
@RestController
@RequestMapping("/news/search")
public class NewsSearchController extends BaseController {

    @Autowired
    private INewsDocumentService newsDocumentService;

    /**
     * 导入新闻数据
     */
    @ApiOperation(value = "导入新闻数据", notes = "导入新闻数据详情")
    @PreAuthorize("@ss.hasPermi('news:search:import')")
    @GetMapping("/import")
    public Result<String> importNews() {
        return toResult(newsDocumentService.importNews());
    }

    /**
     * 分页关键字模糊查询
     */
    @ApiOperation(value = "分页关键字模糊查询", notes = "分页关键字模糊查询详情")
    @GetMapping
    public PageResult<List<NewsDocument>> search(@RequestParam(required = false) String keyword,
                                                 @RequestParam(required = false, defaultValue = "1") Integer page,
                                                 @RequestParam(required = false, defaultValue = "5") Integer size

    ) {
        Page<NewsDocument> newsDocumentPage = newsDocumentService.search(keyword, page, size);
        return ResultUtils.success(page, size, newsDocumentPage.getTotalElements(), newsDocumentPage.getContent());
    }
}
