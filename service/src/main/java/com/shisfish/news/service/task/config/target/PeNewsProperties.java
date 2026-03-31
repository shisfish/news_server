package com.shisfish.news.service.task.config.target;

import lombok.Data;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */
@Data
public class PeNewsProperties {

    /**
     * 请求地址
     */
    public final static String URL = "http://sports.sina.com.cn/nba/";

    /**
     * 详情页目标链接地址css选择器
     */
    public final static String TARGET_URL_CSS_SELECTOR = "div.news-list-b";

    /**
     * 获取所有详情页列表css选择器
     */
    public final static String DETAIL_SELECT_CSS_SELECTOR = ".main-content.w1240";

    /**
     * 标题css选择器
     */
    public final static String NEWS_TITLE_CSS_SELECTOR = ".main-title";

    /**
     * 类型id
     */
    public final static Long NEWS_TYPE_ID = 4L;

    /**
     * 文章内容css选择器
     */
    public final static String NEWS_CONTENT_CSS_SELECTOR = "#artibody";

    /**
     * 文章封面css选择器
     */
    public final static String NEWS_IMAGE_CSS_SELECTOR = ".img_wrapper > img";

    /**
     * key值
     */
    public final static String FIELD_KEY = "peNews";
}
