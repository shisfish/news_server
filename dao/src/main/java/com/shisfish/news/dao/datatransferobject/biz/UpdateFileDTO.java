package com.shisfish.news.dao.datatransferobject.biz;

import lombok.Data;

import java.util.List;

/**
 * @author shuan
 * @date 2023/11/11
 */
@Data
public class UpdateFileDTO {

    /**
     * 需要更新的新闻对象
     */
    private List<NewsWechatDTO> newsWechats;
}
