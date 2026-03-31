package com.shisfish.news.common.lang.enums;

import lombok.Getter;

@Getter
public enum NewsTypeEnum {

    COMMON("common", 0L, "未定义", "biz_06001026_sxdjw_djxwxx"),
    DJDT("djdt", 1L, "党建动态", "biz_06001026_sxdjw_djdtxx"),
    GBGZ("gbgz", 2L, "干部工作", "biz_06001026_sxdjw_gbgzxx"),
    JCDJ("jcdj", 3L, "基层党建", "biz_06001026_sxdjw_jcdjgzxx"),
    RCGZ("rcgz", 4L, "人才工作", "biz_06001026_sxdjw_rcgzxx"),
    ZSJS("zsjs", 5L, "自身建设", "biz_06001026_sxdjw_zsjsgzxx"),
    DJSP("djsp", 6L, "党建视频", "biz_06001026_sxdjw_djspxx"),
    TZGG("tzgg", 7L, "通知公告", "biz_06001026_sxdjw_tzggxx");


    private String table;

    private Long type;

    private String desc;

    private String zfTable;

    NewsTypeEnum(String table, Long type, String desc, String zfTable) {
        this.table = table;
        this.type = type;
        this.desc = desc;
        this.zfTable = zfTable;
    }

    public static NewsTypeEnum getTableString(Long type) {
        for (NewsTypeEnum value : NewsTypeEnum.values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return NewsTypeEnum.COMMON;
    }

}
