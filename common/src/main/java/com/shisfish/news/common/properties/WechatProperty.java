package com.shisfish.news.common.properties;

import com.shisfish.news.dao.plainordinaryjavaobject.biz.WechatColumn;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shisfish
 * @date 2023/8/30
 * @Description 微信对象
 */
@Component
@ConfigurationProperties(prefix = "wechat")
@Data
public class WechatProperty {

    private List<WechatColumn> columns;

}
