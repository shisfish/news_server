package com.shisfish.news.service.mapper.biz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shisfish.news.dao.domain.biz.NewsWechat;

import java.util.List;

/**
 * @author shisfish
 * @date 2023/8/21
 */
public interface NewsWechatMapper extends BaseMapper<NewsWechat>  {
    List<NewsWechat> listWithoutRel();
}
