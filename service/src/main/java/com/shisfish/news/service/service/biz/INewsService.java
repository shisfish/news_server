package com.shisfish.news.service.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shisfish.news.common.request.NewsTypeChangeRequest;
import com.shisfish.news.dao.domain.biz.News;

import java.util.List;
import java.util.Map;

/**
 * @author shisfish
 * @date 2023/8/16
 */
public interface INewsService extends IService<News> {
    /**
     * 根据用户id查询新闻列表
     *
     * @param news   新闻
     * @param userId 用户id
     * @return 新闻集合
     */
    IPage<News> findPageByUserId(Page<News> newsPage, News news, Long userId);

    /**
     * 查询全部新闻列表
     *
     * @param news 新闻
     * @return 新闻集合
     */
    IPage<News> findPageAll(Page<News> newsPage, News news);

    /**
     * 查询新闻
     *
     * @param newsId 新闻ID
     * @return 新闻
     */
    News findNewsById(Long newsId);

    /**
     * 新增新闻
     *
     * @param news 新闻
     * @return 结果
     */
    boolean insertNews(News news);

    /**
     * 修改新闻
     *
     * @param news 新闻
     * @return 结果
     */
    boolean updateNews(News news);

    /**
     * 批量删除新闻
     *
     * @param newsIds 需要删除的新闻ID
     * @return 结果
     */
    boolean deleteNewsByIds(List<Long> newsIds);

    /**
     * 校验新闻标题名称是否唯一
     *
     * @param news 新闻
     * @return 结果
     */
    String checkNewsTitleUnique(News news);

    /**
     * 公开私有修改
     *
     * @param news 新闻
     * @return 结果
     */
    boolean changeIsPublic(News news);

    /**
     * 审核新闻修改
     *
     * @param news 新闻
     * @return 结果
     */
    boolean changeStatus(News news);

    void saveNewsBySpider();


    IPage<News> findPageWithImage(Page<News> newsPage, News news);

    void updateNewsTypeByNewsIds(NewsTypeChangeRequest newsTypeChangeRequest);

    /**
     * 获取已审核数量
     *
     * @param newsIds
     * @return
     */
    Integer countAuditedByNewsIds(List<Long> newsIds);

    void reloadNewsFromNewsWechat(Integer startId);

    List<Long> saveNewsFromWechat();

    void loadNewsToStatic();

    List<News> listUnStatic();

    List<News> findNewsByIds(List<Long> newsIds);

    boolean copyToBack();

    boolean copyToBm();
}
