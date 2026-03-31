package com.shisfish.news.service.task.config.target;

import lombok.Data;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */
@Data
public class EntertainmentNewsProperties {

    /**
     * 请求地址
     */
    public static final String URL = "https://ent.sina.com.cn/";

    /**
     * 详情页目标链接地址css选择器
     */
    public static final String TARGET_URL_CSS_SELECTOR = ".seo_data_list";

    /**
     * 获取所有详情页列表css选择器
     */
    public static final String DETAIL_SELECT_CSS_SELECTOR = ".main-content.w1240";

    /**
     * 标题 css选择器
     */
    public static final String NEWS_TITLE_CSS_SELECTOR = ".main-title";

    /**
     * 类型id
     */
    public static final Long NEWS_TYPE_ID = 16L;

    /**
     * 文章内容css选择器
     */
    public static final String NEWS_CONTENT_CSS_SELECTOR = ".article-content-left";

    /**
     * 文章封面css选择器
     */
    public static final String NEWS_IMAGE_CSS_SELECTOR = ".img_wrapper > img";

    /**
     * key值
     */
    public static final String FIELD_KEY = "entertainmentNews";
}
