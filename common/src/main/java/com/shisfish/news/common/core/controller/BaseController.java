package com.shisfish.news.common.core.controller;

import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.common.utils.string.StringUtils;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: web层通用数据处理
 * @Version: 1.0.0
 */
public class BaseController {
    /**
     * 响应返回结果
     *
     * @param flag 操作成功 true 否则 false
     * @return 操作结果
     */
    protected <T> Result<T> toResult(boolean flag) {
        return flag ? ResultUtils.success() : ResultUtils.error();
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return StringUtils.format("redirect:{}", url);
    }
}
