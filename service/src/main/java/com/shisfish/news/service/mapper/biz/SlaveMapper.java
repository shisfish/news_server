package com.shisfish.news.service.mapper.biz;

import com.shisfish.news.dao.domain.biz.News;
import com.shisfish.news.dao.domain.biz.NewsBase;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SlaveMapper {

    List<Map<String, Object>> findAll(@Param("tableString") String tableString);

    void saveNewsBaseBm(@Param("tableString") String tableString, @Param("newsBase") NewsBase newsBase);
}
