package com.shisfish.news.service.service.biz.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.dao.domain.biz.NewsWechatRel;
import com.shisfish.news.service.mapper.biz.NewsWechatRelMapper;
import com.shisfish.news.service.service.biz.INewsWechatRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shisfish
 * @date 2023/10/26
 */
@Service
public class NewsWechatRelServiceImpl extends ServiceImpl<NewsWechatRelMapper, NewsWechatRel> implements INewsWechatRelService {

    @Autowired
    private NewsWechatRelMapper newsWechatRelMapper;
}
