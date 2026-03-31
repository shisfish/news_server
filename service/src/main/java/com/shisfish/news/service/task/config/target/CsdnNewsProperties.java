package com.shisfish.news.service.task.config.target;

import com.shisfish.news.common.constant.SymbolConstant;
import com.shisfish.news.common.utils.string.StringUtils;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: Csdn爬取配置 注意详细页面css选择范围尽量大点 否则找不到
 * @Version: 1.0.0
 */
@Data
public class CsdnNewsProperties {

    /**
     * 请求地址前缀
     */
    public static final String URL_PREFIX = "https://blog.csdn.net/nav/";

    /**
     * 请求地址后缀 有规律
     */
    public static final String URL_SUFFIX = "java,python,web,arch,5g,db,game,mobile,ops,sec,engineering,iot,fund,avi,other";

    /**
     * 详情页目标链接地址css选择器
     */
    public static final String TARGET_URL_CSS_SELECTOR = ".clearfix";

    /**
     * 获取所有详情页列表css选择器
     */
    public static final String DETAIL_SELECT_CSS_SELECTOR = "div.main_father.clearfix.d-flex.justify-content-center";

    /**
     * 标题css选择器
     */
    public static final String NEWS_TITLE_CSS_SELECTOR = "#articleContentId";

    /**
     * 原创作者选择器
     */
    public static final String NEWS_SOURCE_AUTHOR_CSS_SELECTOR = "#uid > span.name";

    /**
     * 点赞数选择器
     */
    public static final String THUMBS_CSS_SELECTOR = "#asideProfile > div:nth-child(4) > dl:nth-child(5)";

    /**
     * 浏览量选择器
     */
    public static final String VISITS_CSS_SELECTOR = "#asideProfile > div:nth-child(2) > dl:nth-child(1)";

    /**
     * 评论数选择器
     */
    public static final String COMMENTS_CSS_SELECTOR = "#asideProfile > div:nth-child(4) > dl:nth-child(4)";

    /**
     * 收藏数选择器
     */
    public static final String CLLECTS_CSS_SELECTOR = "#asideProfile > div:nth-child(4) > dl:nth-child(5)";

    /**
     * 类型id
     */
    public static final Long NEWS_TYPE_ID = 18L;

    /**
     * 博客文章标签
     */
    public static final String NEWS_SOURCE_TAGS_CSS_SELECTOR = "#mainBox > main > div.blog-content-box > div > div > div.article-info-box > div.blog-tags-box > div.tags-box.artic-tag-box";

    /**
     * 文章内容css选择器
     */
    public static final String NEWS_CONTENT_CSS_SELECTOR = "#article_content";

    /**
     * 文章封面css选择器
     */
    public static final String NEWS_IMAGE_CSS_SELECTOR = "img.avatar_pic";

    /**
     * key值
     */
    public static final String FIELD_KEY = "csdnNews";


    /**
     * 拼接初始化的URL
     */
    public static List<String> getInitUrlList() {
        List<String> initCrawlerUrlList = null;
        // 前缀
        if (StringUtils.isNotEmpty(URL_PREFIX)) {
            // 后缀逗号取出
            String[] initCrawlerUrlArray = URL_SUFFIX.split(SymbolConstant.COMMA);
            if (initCrawlerUrlArray.length > 0) {
                for (int i = 0; i < initCrawlerUrlArray.length; i++) {
                    String initUrl = initCrawlerUrlArray[i];
                    if (StringUtils.isNotEmpty(initUrl)) {
                        if (!initUrl.toLowerCase().startsWith("http")) {
                            initUrl = URL_PREFIX + initUrl;
                            initCrawlerUrlArray[i] = initUrl;
                        }
                    }
                }
            }
            initCrawlerUrlList = Arrays.stream(initCrawlerUrlArray).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        }
        return initCrawlerUrlList;
    }
}
