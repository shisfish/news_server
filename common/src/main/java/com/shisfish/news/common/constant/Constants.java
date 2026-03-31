package com.shisfish.news.common.constant;

/**
 * 常用静态常量
 */
public class Constants {

    /**
     * 用于IP定位转换
     */
    public static final String REGION = "内网IP|内网IP";
    /**
     * win 系统
     */
    public static final String WIN = "win";

    /**
     * mac 系统
     */
    public static final String MAC = "mac";

    /**
     * 常用接口
     */
    public static class Url {
        // 免费图床
        public static final String SM_MS_URL = "https://sm.ms/api";
        // IP归属地查询
        public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";
    }

    /**
     * 删除标记
     */
    public static class DeleteFlag {
        /**
         * 未删除
         */
        public static final int NOT_DELETE = 0;
        /**
         * 已删除
         */
        public static final int DELETED = 1;
    }

    /**
     * 启用禁用标记
     */
    public static class AbleFlag {
        /**
         * 启用
         */
        public static final int ENABLE = 0;
        /**
         * 禁用
         */
        public static final int DISABLE = 1;
    }


    /**
     * 审核状态
     */
    public static class AuditFlag {
        /**
         * 审核中
         */
        public static final int AUDITING = 0;
        /**
         * 审核通过
         */
        public static final int AUDIT_PASSED = 1;
        /**
         * 审核未通过
         */
        public static final int AUDIT_UNPASS = 2;
    }

    public static class NewsSource {
        /**
         * 系统自建
         */
        public static final String SYSTEM = "SYSTEM";

        /**
         * 微信
         */
        public static final String WECHAT = "WECHAT";
    }

    /**
     * 处理标识
     */
    public static class DealFlag {
        /**
         * 未处理
         */
        public static final int UN_DEAL = 0;
        /**
         * 已处理
         */
        public static final int DEALED = 1;
    }




    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";
    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";


    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/resource";

    public static final String DEFAULT_USER = "system";
}
