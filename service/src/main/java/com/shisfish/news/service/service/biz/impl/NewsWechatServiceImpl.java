package com.shisfish.news.service.service.biz.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.common.constant.WechatConstant;
import com.shisfish.news.dao.datatransferobject.biz.SpiderNewsDTO;
import com.shisfish.news.dao.plainordinaryjavaobject.biz.WechatColumn;
import com.shisfish.news.common.properties.WechatProperty;
import com.shisfish.news.dao.domain.biz.NewsWechat;
import com.shisfish.news.service.mapper.biz.NewsWechatMapper;
import com.shisfish.news.service.service.biz.INewsService;
import com.shisfish.news.service.service.biz.INewsWechatService;
import com.shisfish.news.service.service.spider.SpiderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shisfish
 * @date 2023/8/21
 */
@Service
public class NewsWechatServiceImpl extends ServiceImpl<NewsWechatMapper, NewsWechat> implements INewsWechatService {

    @Autowired
    private NewsWechatMapper newsWechatMapper;

    @Autowired
    private WechatProperty wechatProperty;

    @Autowired
    private INewsService newsService;

    @Override
    public NewsWechat findByAid(String aid) {
        return newsWechatMapper.selectOne(Wrappers.<NewsWechat>lambdaQuery()
                .eq(NewsWechat::getAid, aid)
                .last("limit 1"));
    }

    @Override
    public Integer countByAid(String aid) {
        return newsWechatMapper.selectCount(Wrappers.<NewsWechat>lambdaQuery()
                .eq(NewsWechat::getAid, aid));
    }

    public Integer countByLink(String link) {
        return newsWechatMapper.selectCount(Wrappers.<NewsWechat>lambdaQuery()
                .eq(NewsWechat::getLink, link));
    }

    @Override
    public List<NewsWechat> getLatestWechatBySpider(WechatColumn wechatColumn) {
        return SpiderUtil.getArticlesHomepage(wechatColumn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<NewsWechat> saveNewsWechatDeDuplicationReturnNew(List<NewsWechat> newsWechats) {
        List<NewsWechat> newList = new ArrayList<>();
        for (NewsWechat newsWechat : newsWechats) {
            if (countByAid(newsWechat.getAid()) == 0) {
                newsWechatMapper.insert(newsWechat);
                newList.add(newsWechat);
            }
        }
        return newList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNewsWechatDeDuplication(List<NewsWechat> newsWechats) {
        for (NewsWechat newsWechat : newsWechats) {
            if (countByAid(newsWechat.getAid()) == 0) {
                newsWechatMapper.insert(newsWechat);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<NewsWechat> getNewNewsWechatBySpider() {
        List<WechatColumn> columns = wechatProperty.getColumns();
        if (CollectionUtil.isEmpty(columns)) {
            return null;
        }
        List<NewsWechat> list = new ArrayList<>();
        for (WechatColumn column : columns) {
            List<NewsWechat> newsWechats = getLatestWechatBySpider(column);
            list.addAll(newsWechats);
        }
        return saveNewsWechatDeDuplicationReturnNew(list);
    }

    @Override
    public List<NewsWechat> getNewNewsWechat(Integer startId) {
        return newsWechatMapper.selectList(Wrappers.<NewsWechat>lambdaQuery()
                        .ge(NewsWechat::getId, startId));
    }

    @Override
    public void saveLatestNewsWechatBySpider() {
        List<WechatColumn> columns = wechatProperty.getColumns();
        if (CollectionUtil.isEmpty(columns)) {
            return ;
        }
        List<NewsWechat> list = new ArrayList<>();
        for (WechatColumn column : columns) {
            List<NewsWechat> newsWechats = getLatestWechatBySpider(column);
            list.addAll(newsWechats);
        }
        saveNewsWechatDeDuplication(list);
    }

    @Override
    public List<NewsWechat> listWithoutRel() {
        return newsWechatMapper.listWithoutRel();
    }

    @Override
    public void saveLatestNewsWechatFromApp(Integer begin, String token, String cookie) {
        List<NewsWechat> list = SpiderUtil.getArticlesHomepage(WechatConstant.SHAOXINGZUGONG, token, cookie, begin);
        saveNewsWechatDeDuplication(list);
    }

    @Override
    public List<Long> spiderNews(SpiderNewsDTO spiderNewsDTO) {
        int begin = 0;
        // 获取当前总条数
        Integer beforeSum = countAll();
        Integer afterSum = beforeSum;
        do {
            saveLatestNewsWechatFromApp(begin, spiderNewsDTO.getToken(), spiderNewsDTO.getCookie());
            // 再次获取当前总条数
            afterSum = countAll();
            Integer addNum = afterSum - beforeSum;
            if (addNum == 0) {
                break;
            } else {
                begin += addNum;
                beforeSum = afterSum;
            }
            // 做个限制，防止一直请求
            if (begin > 30) {
                break;
            }
        } while(true);


        if (begin == 0) {
            return new ArrayList<>();
        }
        // 只有有拉取过，才进行本地化落库
        return newsService.saveNewsFromWechat();
    }

    @Override
    public List<Long> spiderShaoxingZugong() {
        List<NewsWechat> list = SpiderUtil.searchShaoxingZugongArticles();
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<NewsWechat> newList = saveNewsWechatDeDuplicationReturnNew(list);
        if (CollectionUtil.isEmpty(newList)) {
            return new ArrayList<>();
        }
        return newsService.saveNewsFromWechat();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> spiderNewsByConfig() {
        List<WechatColumn> columns = wechatProperty.getColumns();
        if (CollectionUtil.isEmpty(columns)) {
            return new ArrayList<>();
        }
        List<NewsWechat> list = new ArrayList<>();
        for (WechatColumn column : columns) {
            List<NewsWechat> newsWechats = getLatestWechatBySpider(column);
            if (CollectionUtil.isNotEmpty(newsWechats)) {
                list.addAll(newsWechats);
            }
        }
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<NewsWechat> newList = saveNewsWechatDeDuplicationReturnNew(list);
        if (CollectionUtil.isEmpty(newList)) {
            return new ArrayList<>();
        }
        return newsService.saveNewsFromWechat();
    }

    @Override
    public Integer countAll() {
        return newsWechatMapper.selectCount(Wrappers.lambdaQuery());
    }

    private boolean existsNewsWechat(NewsWechat newsWechat) {
        if (newsWechat == null) {
            return true;
        }
        if (newsWechat.getAid() != null && countByAid(newsWechat.getAid()) > 0) {
            return true;
        }
        return newsWechat.getLink() != null && countByLink(newsWechat.getLink()) > 0;
    }
}
