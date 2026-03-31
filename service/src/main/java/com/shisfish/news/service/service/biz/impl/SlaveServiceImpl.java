package com.shisfish.news.service.service.biz.impl;

import com.shisfish.news.common.config.DataSource;
import com.shisfish.news.common.constant.DataSourceType;
import com.shisfish.news.dao.domain.biz.NewsBase;
import com.shisfish.news.service.mapper.biz.SlaveMapper;
import com.shisfish.news.service.service.biz.ISlaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author huangshun
 * @date 2024-11-11 10:35
 * @description:
 */
@Service
public class SlaveServiceImpl implements ISlaveService {

    @Autowired
    private SlaveMapper slaveMapper;

    @Override
    @DataSource(value = DataSourceType.SLAVE)
    public void saveNewsBaseBm(String tableString, NewsBase news) {
        slaveMapper.saveNewsBaseBm(tableString, news);
    }

    @Override
    @DataSource(value = DataSourceType.SLAVE)
    public List<Map<String, Object>> testSlave(String tableName) {
        return slaveMapper.findAll(tableName);
    }

}
