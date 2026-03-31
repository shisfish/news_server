package com.shisfish.news.service.service.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.shisfish.news.common.constant.Constants;
import com.shisfish.news.common.constant.UserConstants;
import com.shisfish.news.dao.domain.system.SysUser;
import com.shisfish.news.common.exception.CustomException;
import com.shisfish.news.common.utils.string.StringUtils;
import com.shisfish.news.dao.domain.biz.News;
import com.shisfish.news.dao.domain.biz.NewsDocument;
import com.shisfish.news.dao.domain.biz.NewsType;
import com.shisfish.news.service.mapper.biz.NewsDocumentDao;
import com.shisfish.news.service.service.biz.INewsDocumentService;
import com.shisfish.news.service.service.biz.INewsService;
import com.shisfish.news.service.service.biz.INewsTypeService;
import com.shisfish.news.service.service.system.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */
@Slf4j
@Service
public class NewsDocumentServiceImpl implements INewsDocumentService {
    @Autowired
    private NewsDocumentDao newsDocumentDao;
    @Autowired
    private INewsService newsService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private INewsTypeService newsTypeService;

    /**
     * 导入新闻数据
     */
    @Override
    public boolean importNews() {
        List<News> newsList = newsService
                .list(new LambdaQueryWrapper<News>()
                        // 未删除
                        .eq(News::getDelFlag, UserConstants.NORMAL)
                        // 公开
                        .eq(News::getIsPublic, UserConstants.NewsPublic.PUBLIC)
                        // 审核成功
                        .eq(News::getStatus, Constants.AuditFlag.AUDIT_PASSED)
                );
        List<NewsDocument> newsDocumentList = new ArrayList<>();
        newsList.stream().filter(Objects::nonNull).forEach(news -> {
            newsDocumentList.add(convertNewsToNewsDocument(news));
        });
//        Iterable<NewsDocument> iterable = newsDocumentDao.saveAll(newsDocumentList);
//        Iterator<NewsDocument> iterator = iterable.iterator();
        int ret = 0;
//        if (iterator.hasNext()) {
//            ret++;
//            iterator.next();
//        }
        return ret > 0;
    }

    /**
     * 将News转换为NewsDocument
     *
     * @param news
     * @return
     */
    private NewsDocument convertNewsToNewsDocument(News news) {
        // 创建 NewsDocument
        NewsDocument newsDocument = new NewsDocument();
        newsDocument
                // 新闻id
                .setNewsId(news.getNewsId())
                // 作者
                .setAuthor(userService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserId, news.getUserId())
                        .select(SysUser::getUsername, SysUser::getAvatar, SysUser::getPhone, SysUser::getEmail, SysUser::getSex, SysUser::getNickName)))
                // 新闻栏目
                .setNewsType(newsTypeService.getOne(new LambdaQueryWrapper<NewsType>().eq(NewsType::getNewsTypeId, news.getNewsTypeId())
                        .select(NewsType::getNewsTypeId, NewsType::getNewsTypeName)))
                // 新闻标题
                .setNewsTitle(news.getNewsTitle())
                // 新闻内容
                .setNewsContent(news.getNewsContent());
        switch (news.getNewsAttr()) {
            // 新闻属性
            case "0":
                newsDocument.setNewsAttr(UserConstants.HEADLINE_AREA_NEWS);
                break;
            case "1":
                newsDocument.setNewsAttr(UserConstants.SLIDE_SHOW_NEWS);
                break;
            case "2":
                newsDocument.setNewsAttr(UserConstants.HOT_SPOT_NEWS);
                break;
            default:
                break;
        }
        newsDocument
                // 新闻封面
                .setNewsImage(news.getNewsImage())
                // 新闻点赞数
                .setThumbs(news.getThumbs())
                // 新闻浏览量
                .setVisits(news.getVisits())
                // 新闻评论数
                .setComments(news.getComments())
                // 新闻来源
                .setNewsSource(news.getNewsSource())
                // 新闻博客原创作者(博客使用)
                .setNewsSourceAuthor(news.getNewsSourceAuthor())
                // 新闻博客分类标签
                .setNewsSourceTags(news.getNewsSourceTags())
                // 创建者
                .setCreateBy(news.getCreateBy())
                // 创建时间
                .setCreateTime(news.getCreateTime())
        ;
        log.info("\n正在转换News------>NewsDocument---->当前时间: {}", System.currentTimeMillis());
        return newsDocument;
    }

    /**
     * 根据id删除新闻
     *
     * @param newsId 新闻id
     */
    @Override
    public void deleteByNewsId(Long newsId) {
//        newsDocumentDao.deleteById(newsId);
    }

    /**
     * 查询新闻
     *
     * @param newsId 新闻ID
     * @return 新闻
     */
    @Override
    public NewsDocument findNewsById(Long newsId) {
//        return newsDocumentDao.findById(newsId).get();
        return null;
    }

    /**
     * 新增新闻
     *
     * @param news 新闻
     * @return 结果
     */
    @Override
    public void saveNews(News news) {
        // 先识别新闻id是否为空
        Long newsId = StringUtils.isNull(news.getNewsId()) ? -1L : news.getNewsId();
        NewsDocument info = newsDocumentDao.findByNewsTitle(news.getNewsTitle());
        if (StringUtils.isNotNull(info) && !Objects.equals(info.getNewsId(), newsId)) {
            throw new CustomException("新增新闻'" + news.getNewsTitle() + "'失败，新闻标题已存在");
        }
        // 解析News 构建NewsDocument
//        newsDocumentDao.save(convertNewsToNewsDocument(news));
    }


    /**
     * 批量删除新闻
     *
     * @param newsIds 需要删除的新闻ID
     * @return 结果
     */
    @Override
    public void deleteNewsByIds(List<Long> newsIds) {
        if (CollectionUtils.isNotEmpty(newsIds)) {
            List<NewsDocument> newsDocumentList = new ArrayList<>();
            newsIds.stream().filter(Objects::nonNull)
                    .forEach(newsId -> {
                        NewsDocument newsDocument = new NewsDocument();
                        newsDocument.setNewsId(newsId);
                        newsDocumentList.add(newsDocument);
                    });
//            newsDocumentDao.deleteAll(newsDocumentList);
        }
    }

    /**
     * 分页根据关键字搜索
     */
    @Override
    public Page<NewsDocument> search(String keyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return newsDocumentDao.findAllByNewsTitleLikeOrNewsContentLikeOrNewsAttrLikeOrNewsSourceAuthorLikeOrNewsSourceTagsLikeOrCreateByLike(keyword, keyword, keyword, keyword, keyword, keyword, pageable);
    }
}
