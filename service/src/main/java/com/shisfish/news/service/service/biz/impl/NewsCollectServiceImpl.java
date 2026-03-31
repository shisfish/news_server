package com.shisfish.news.service.service.biz.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.dao.domain.biz.NewsCollect;
import com.shisfish.news.service.mapper.biz.NewsCollectMapper;
import com.shisfish.news.service.service.biz.INewsCollectService;
import org.springframework.stereotype.Service;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

@Service
public class NewsCollectServiceImpl extends ServiceImpl<NewsCollectMapper, NewsCollect> implements INewsCollectService {

}
