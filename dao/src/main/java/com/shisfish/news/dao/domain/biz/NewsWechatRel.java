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
 * @date 2023/10/26
 * @Description 微信公众号和新闻关联表
 */
@Data
@TableName(value = "news_wechat_rel")
public class NewsWechatRel {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long newsWechatId;

    private Long newsId;

    private Long sendtime;

    @TableField(value = "create_time")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createTime;

}
