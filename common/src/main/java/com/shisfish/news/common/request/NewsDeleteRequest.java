package com.shisfish.news.common.request;

import lombok.Data;

import java.util.List;

/**
 * @author shisfish
 * @date 2023/9/5
 * @Description 新闻删除方法
 */
@Data
public class NewsDeleteRequest {

    /**
     * 新闻对象
     */
    private List<Long> newsIds;

}
