package com.shisfish.news.common.constant;

/**
 * 用户常量信息
 */
public class UserConstants {

    /**
     * 正常状态
     */
    public static final String NORMAL = "0";

    /**
     * 异常状态
     */
    public static final String EXCEPTION = "1";

    /**
     * 用户封禁状态
     */
    public static final String USER_DISABLE = "1";

    /**
     * 角色正常状态
     */
    public static final String ROLE_NORMAL = "0";

    /**
     * 角色封禁状态
     */
    public static final String ROLE_DISABLE = "1";

    /**
     * 部门正常状态
     */
    public static final String DEPT_NORMAL = "0";

    /**
     * 部门停用状态
     */
    public static final String DEPT_DISABLE = "1";

    /**
     * 字典正常状态
     */
    public static final String DICT_NORMAL = "0";

    /**
     * 是否为系统默认（是）
     */
    public static final String YES = "Y";

    /**
     * 是否菜单外链（是）
     */
    public static final String YES_FRAME = "0";

    /**
     * 是否菜单外链（否）
     */
    public static final String NO_FRAME = "1";

    /**
     * Layout组件标识
     */
    public final static String LAYOUT = "Layout";

    /**
     * 校验返回结果码
     */
    public final static String UNIQUE = "0";
    public final static String NOT_UNIQUE = "1";

    /**
     * 新闻属性 0.头条区新闻 1.幻灯片区新闻 2.热点区新闻
     * Headline area news
     * Slide show news
     * Hot spot news
     */
    public final static String HEADLINE_AREA_NEWS = "头条区新闻";
    public final static String SLIDE_SHOW_NEWS = "幻灯片区新闻";
    public final static String HOT_SPOT_NEWS = "热点区新闻";


    /**
     * 新闻公开状态
     */
    public static class NewsPublic {
        /**
         * 公开
         */
        public static final int PUBLIC = 0;
        /**
         * 私有
         */
        public static final int PRIVATE = 1;
    }

    /**
     * 用户密码变更类型
     * 1 新增用户
     * 2 修改密码
     * 3 重置密码
     */
    /**
     * 新增用户
     */
    public final static int PASSWORD_LOG_TYPE_ADD = 1;
    /**
     * 修改密码
     */
    public final static int PASSWORD_LOG_TYPE_UPDATE = 2;
    /**
     * 重置密码
     */
    public final static int PASSWORD_LOG_TYPE_RESET = 3;


}
