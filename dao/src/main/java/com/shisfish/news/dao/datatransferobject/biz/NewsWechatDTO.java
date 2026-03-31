package com.shisfish.news.dao.datatransferobject.biz;

import lombok.Data;

/**
 * @author shuan
 * @date 2023/11/4
 */
@Data
public class NewsWechatDTO {

    /**
     * 文章唯一标识
     */
    private String aid;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String cover;

    /**
     * 连接地址
     */
    private String link;

    /**
     * appmsgid
     */
    private String appmsgid;

    /**
     * 发布时间
     */
    private Long sendtime;

    /**
     * 封面是否处理过
     */
    private Integer dealCover = 0;

    /**
     * 封面名称
     */
    private String coverName;

    /**
     * 静态是否处理过
     */
    private Integer dealStatic = 0;

    /**
     * 静态资源名
     */
    private String staticName;

    /**
     * 是否压缩
     */
    private Integer dealZip = 0;

    /**
     * 是否已经被上传了
     */
    private Integer dealUpload = 0;

}
