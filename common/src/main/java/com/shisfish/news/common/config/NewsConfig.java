package com.shisfish.news.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 * @author shisfish
 */
@Component
@ConfigurationProperties(prefix = "news")
public class NewsConfig {
    /**
     * 项目名称
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * 版权年份
     */
    private String copyrightYear;

    private String localFileUrl;

    /**
     * 上传路径
     */
    private static String localFilePath;

    /**
     * 新服务器地址
     */
    private String newServerUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCopyrightYear() {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear) {
        this.copyrightYear = copyrightYear;
    }

    public String getLocalFileUrl() {
        return this.localFileUrl;
    }

    public void setLocalFileUrl(String localFileUrl) {
        this.localFileUrl = localFileUrl;
    }

    public String getNewServerUrl() {
        return this.newServerUrl;
    }

    public void setNewServerUrl(String newServerUrl) {
        this.newServerUrl = newServerUrl;
    }

    /**
     * 获取地址开关
     */
    private static boolean addressEnabled;

    public static String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        NewsConfig.localFilePath = localFilePath;
    }

    public static boolean isAddressEnabled() {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled) {
        NewsConfig.addressEnabled = addressEnabled;
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return getLocalFilePath() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getLocalFilePath() + "/download";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        return getLocalFilePath() + "/upload";
    }

    public static String getHtmlDownloadPath() {
        return getLocalFilePath() + "/html";
    }
}
