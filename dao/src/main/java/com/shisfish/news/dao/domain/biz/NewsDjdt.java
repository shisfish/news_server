package com.shisfish.news.dao.domain.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "news_djdt")
public class NewsDjdt extends NewsBase {
}
