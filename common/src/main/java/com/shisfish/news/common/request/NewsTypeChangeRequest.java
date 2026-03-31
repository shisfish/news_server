package com.shisfish.news.common.request;

import lombok.Data;

import java.util.List;

/**
 * @author shisfish
 * @date 2023/9/4
 * @Description 新闻分类批量更新对象
 */
@Data
public class NewsTypeChangeRequest {

    /**
     * 分类
     */
    private Long newsTypeId;

    /**
     * 新闻对象
     */
    private List<Long> newsIds;

}
