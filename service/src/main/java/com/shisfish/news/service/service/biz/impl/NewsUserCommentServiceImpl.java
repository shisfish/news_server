package com.shisfish.news.service.service.biz.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.dao.domain.biz.NewsUserComment;
import com.shisfish.news.service.mapper.biz.NewsUserCommentMapper;
import com.shisfish.news.service.service.biz.INewsUserCommentService;
import org.springframework.stereotype.Service;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

@Service
public class NewsUserCommentServiceImpl extends ServiceImpl<NewsUserCommentMapper, NewsUserComment> implements INewsUserCommentService {

}
