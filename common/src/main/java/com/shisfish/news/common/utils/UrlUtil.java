package com.shisfish.news.common.utils;

/**
 * @author shisfish
 * @date 2023/10/27
 * @Description 地址处理工具
 */
public class UrlUtil {

    public static String removeParam(String url, String... name) {
        for (String s : name) {
            // 使用replaceAll正则替换,replace不支持正则
            url = url.replaceAll("&&?" + s + "=[^&]*", "");
        }
        return url;
    }

}
