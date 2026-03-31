package com.shisfish.news.service.service.biz;

import com.shisfish.news.dao.domain.biz.NewsBase;

import java.util.List;
import java.util.Map;

/**
 * @author huangshun
 * @date 2024-11-11 10:35
 * @description:
 */
public interface ISlaveService {

    void saveNewsBaseBm(String tableString, NewsBase news);

    List<Map<String, Object>> testSlave(String tableName);

}
