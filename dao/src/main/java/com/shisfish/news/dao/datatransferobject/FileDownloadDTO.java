package com.shisfish.news.dao.datatransferobject;

import lombok.Data;

/**
 * @author shuan
 * @date 2023/10/26
 */
@Data
public class FileDownloadDTO {

    /**
     * 下载地址
     */
    private String url;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 下载全路径
     */
    private String downloadFullPath;
    
}
