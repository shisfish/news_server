package com.shisfish.news.service.service.biz;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shisfish.news.dao.datatransferobject.biz.SpiderNewsDTO;
import com.shisfish.news.dao.plainordinaryjavaobject.biz.WechatColumn;
import com.shisfish.news.dao.domain.biz.NewsWechat;

import java.util.List;

/**
 * @author shisfish
 * @date 2023/8/21
 */
public interface INewsWechatService extends IService<NewsWechat> {

    NewsWechat findByAid(String aid);

    Integer countByAid(String aid);

    List<NewsWechat> getLatestWechatBySpider(WechatColumn wechatColumn);

    List<NewsWechat> saveNewsWechatDeDuplicationReturnNew(List<NewsWechat> newsWechats);

    void saveNewsWechatDeDuplication(List<NewsWechat> newsWechats);

    List<NewsWechat> getNewNewsWechatBySpider();

    List<NewsWechat> getNewNewsWechat(Integer startId);

    void saveLatestNewsWechatBySpider();

    List<NewsWechat> listWithoutRel();

    void saveLatestNewsWechatFromApp(Integer begin, String token, String cookie);

    List<Long> spiderNews(SpiderNewsDTO spiderNewsDTO);

    Integer countAll();
}
