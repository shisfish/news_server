package com.shisfish.news.dao.domain.biz;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author shisfish
 * @date 2023/8/21
 */

@Data
@TableName(value = "news_wechat")
public class NewsWechat {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String aid;

    private String title;

    private String cover;

    private String link;

    private String appmsgid;

    /**
     * 发布时间-时间戳
     */
    private Long sendtime;

    @TableField(value = "create_time")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createTime;

}
