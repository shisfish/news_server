package com.shisfish.news.service.mapper.biz;

import com.shisfish.news.dao.domain.biz.NewsDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */
public interface NewsDocumentDao
//        extends ElasticsearchRepository<NewsDocument, Long>
{
    /**
     * 根据新闻标题查询
     *
     * @param newsTitle 新闻标题
     *
     * @return NewsDocument
     */
    NewsDocument findByNewsTitle(String newsTitle);

    /**
     * 分页条件模糊查询
     * @param newsTitle 新闻标题
     * @param newsContent 新闻内容
     * @param newsAttr 新闻属性
     * @param newsSourceAuthor 新闻来源作者
     * @param newsSourceTags 新闻博客分类标签
     * @param createBy 编辑人
     * @param pageable 分页对象
     * @return
     */
    Page<NewsDocument> findAllByNewsTitleLikeOrNewsContentLikeOrNewsAttrLikeOrNewsSourceAuthorLikeOrNewsSourceTagsLikeOrCreateByLike(String newsTitle, String newsContent, String newsAttr, String newsSourceAuthor, String newsSourceTags, String createBy, Pageable pageable);

}
