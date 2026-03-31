package com.shisfish.news.service.service.biz.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.common.config.DataSource;
import com.shisfish.news.common.constant.Constants;
import com.shisfish.news.common.constant.DataSourceType;
import com.shisfish.news.common.constant.UserConstants;
import com.shisfish.news.common.exception.CustomException;
import com.shisfish.news.common.lang.enums.NewsTypeEnum;
import com.shisfish.news.common.request.NewsTypeChangeRequest;
import com.shisfish.news.common.utils.DateUtils;
import com.shisfish.news.common.utils.SecurityUtils;
import com.shisfish.news.common.utils.UrlUtil;
import com.shisfish.news.common.utils.file.FileUtil;
import com.shisfish.news.common.utils.string.StringUtils;
import com.shisfish.news.common.utils.uuid.IdUtils;
import com.shisfish.news.dao.domain.biz.*;
import com.shisfish.news.dao.datatransferobject.FileDownloadDTO;
import com.shisfish.news.dao.domain.system.SysDictData;
import com.shisfish.news.service.mapper.biz.NewsMapper;
import com.shisfish.news.service.mapper.biz.SlaveMapper;
import com.shisfish.news.service.service.biz.INewsDocumentService;
import com.shisfish.news.service.service.biz.INewsService;
import com.shisfish.news.service.service.biz.INewsWechatRelService;
import com.shisfish.news.service.service.biz.INewsWechatService;
import com.shisfish.news.service.service.biz.ISlaveService;
import com.shisfish.news.service.service.system.ISysDictDataService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.DateUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author shisfish
 * @date 2023/8/16
 */
@Slf4j
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements INewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private INewsDocumentService newsDocumentService;

    @Autowired
    private INewsWechatService newsWechatService;

    @Autowired
    private INewsWechatRelService newsWechatRelService;

    @Autowired
    private ISysDictDataService sysDictDataService;

    /**
     * 根据用户id查询新闻列表
     *
     * @param news   新闻
     * @param userId 用户id
     * @return 新闻集合
     */
    @Override
    public IPage<News> findPageByUserId(Page<News> newsPage, News news, Long userId) {
        return baseMapper.findPageByUserId(newsPage, news, userId);
    }

    /**
     * 查询新闻列表
     *
     * @param news 新闻
     * @return 新闻集合
     */
    @Override
    public IPage<News> findPageAll(Page<News> newsPage, News news) {
        return baseMapper.findPageAll(newsPage, news);
    }

    /**
     * 查询新闻
     *
     * @param newsId 新闻ID
     * @return 新闻
     */
    @Override
    public News findNewsById(Long newsId) {
        return getById(newsId);
    }

    /**
     * 新增新闻
     *
     * @param news 新闻
     * @return 结果
     */
    @Override
    public boolean insertNews(News news) {
        // 设置发布时间
        if (news.getPublishTime() == null) {
            news.setPublishTime(new Date());
        }
        // 设置作者id
        news.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        news.setNewsSource(Constants.NewsSource.SYSTEM);
        news.setCreateBy(SecurityUtils.getUsername());
        news.setCreateTime(new Date());
        news.setUpdateBy(SecurityUtils.getUsername());
        news.setUpdateTime(new Date());
        return saveOrUpdate(news);
    }

    /**
     * 修改新闻
     *
     * @param news 新闻
     * @return 结果
     */
    @Override
    public boolean updateNews(News news) {
        // 设置发布时间
        if (news.getPublishTime() == null) {
            news.setPublishTime(new Date());
        }
        // 更新时间
        news.setUpdateTime(new Date());
        // 修改人
        news.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUsername());
        return saveOrUpdate(news);
    }

    /**
     * 批量删除新闻
     *
     * @param newsIds 需要删除的新闻ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteNewsByIds(List<Long> newsIds) {
        /**
         * 判断全局检索中是否存在 如果存在就将全局检索中的也删除
         */
//        newsIds.stream().filter(Objects::nonNull)
//                .forEach(newsId -> {
//                    // 查出这个新闻
//                    News one = getById(newsId);
//                    // 根据id查询 全局检索中是否存在该新闻 存在就删除
//                    checkExistedInESAndRemove(one);
//                });
        // 验证是否存在审核成功的数据，有的话不允许删除
        Integer auditedCount = countAuditedByNewsIds(newsIds);
        if (auditedCount > 0) {
            throw new CustomException("删除新闻中存在审核成功新闻，更改状态后再进行删除");
        }
        return removeByIds(newsIds);
    }

    /**
     * 校验新闻标题名称是否唯一
     *
     * @param news 新闻
     * @return 结果
     */
    @Override
    public String checkNewsTitleUnique(News news) {
        // 先识别新闻id是否为空
        Long newsId = StringUtils.isNull(news.getNewsId()) ? -1L : news.getNewsId();
        // 根据新闻名查询
        News info = getOne(new LambdaQueryWrapper<News>().eq(News::getNewsTitle, news.getNewsTitle()));
        if (StringUtils.isNotNull(info) && !Objects.equals(info.getNewsId(), newsId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 公开私有修改
     *
     * @param news 新闻
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean changeIsPublic(News news) {
        /**
         * 修复bug  java.lang.NullPointerException: null
         */
        // 查询出当前所有信息
        news = getById(news.getNewsId());
        /**
         * 查询全局检索中是否存在该新闻
         * 我们只有 公开的时候才放到全局检索中去 否则 从全局检索中删除
         * 在这些的前提前必须是 审核通过
         */
        switch (news.getIsPublic()) {
            // 公开
            case 0:
                // 判断是否审核通过
                if (news.getStatus().equals(Constants.AuditFlag.AUDIT_PASSED)) {
                    newsDocumentService.saveNews(news);
                }
                break;
            // 私有
            case 1:
                // 根据id查询 全局检索中是否存在该新闻 存在就删除
                checkExistedInESAndRemove(news);
                break;
            default:
                break;
        }
        // 更新时间
        news.setUpdateTime(new Date());
        // 修改人
        news.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUsername());
        return saveOrUpdate(news);
    }

    /**
     * 审核新闻修改
     *
     * @param news 新闻
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean changeStatus(News news) {
        /**
         *   都会先查询是全局检索中是否存在这个新闻
         *   审核通过了: 就增加到全局检索 --> 公开的话才放到全局检索 不公开就放到全局检索
         *   审核中或者审核失败: 存在就进行删除
         *
         */
//        switch (news.getStatus()) {
//            case 1: // 审核通过 就增加到全局检索
//                if (news.getIsPublic().equals(UserConstants.PUBLIC)) {
//                    newsDocumentService.saveNews(news);
//                }
//                break;
//            case 0: // 审核中或者审核失败
//            case 2:
//                // 根据id查询 全局检索中是否存在该新闻 存在就删除
//                checkExistedInESAndRemove(news);
//                break;
//            default:
//                break;
//        }
        News oldNews = findNewsById(news.getNewsId());
        oldNews.setStatus(news.getStatus());
        return updateById(oldNews);
    }

    /**
     * 判断ES中是否存在该条新闻 存在就删除
     *
     * @param news 新闻
     */
    private void checkExistedInESAndRemove(News news) {
        log.info("判断ES中是否存在该条新闻--->{}", news);
        // 先识别新闻
        NewsDocument newsDocument = newsDocumentService.findNewsById(news.getNewsId());
        // 查到了该新闻
        if (StringUtils.isNotNull(newsDocument.getNewsId())) {
            newsDocumentService.deleteByNewsId(newsDocument.getNewsId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNewsBySpider() {
        List<NewsWechat> newsWechats = newsWechatService.getNewNewsWechatBySpider();
        if (CollectionUtil.isEmpty(newsWechats)) {
            return;
        }
        // 转化成News对象
        for (NewsWechat newsWechat : newsWechats) {
            News news = new News();
            news.setUserId(18L);
            news.setNewsTypeId(null);
            news.setNewsTitle(newsWechat.getTitle());
            news.setType(2);
            news.setLink(newsWechat.getLink());
            news.setPublishTime(new Date(newsWechat.getSendtime() * 1000));
            news.setCreateTime(new Date());
            news.setCreateBy(Constants.DEFAULT_USER);
            news.setUpdateTime(new Date());
            news.setUpdateBy(Constants.DEFAULT_USER);
            news.setStatus(0);
            news.setNewsImage(newsWechat.getCover());
            try {
                // 图片需要上传
                // 默认图片是300像素，尝试是否可以得到640像素的图片
                if (newsWechat.getCover().endsWith("/300")) {
                    int coverLength = newsWechat.getCover().length();
                    newsWechat.setCover(newsWechat.getCover().substring(0, coverLength - 4) + "/640");
                }
                String filePath = FileUtil.downloadImage(newsWechat.getCover());
                news.setNewsImage(filePath);
            } catch (Exception e) {
                log.error("下载微信图片失败：", e.getMessage());
            }
            newsMapper.insert(news);
        }
    }

    @Override
    public IPage<News> findPageWithImage(Page<News> newsPage, News news) {
        return baseMapper.findPageWithImage(newsPage, news);
    }

    @Override
    public void updateNewsTypeByNewsIds(NewsTypeChangeRequest newsTypeChangeRequest) {
        LambdaUpdateWrapper<News> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(News::getNewsId, newsTypeChangeRequest.getNewsIds());
        updateWrapper.set(News::getNewsTypeId, newsTypeChangeRequest.getNewsTypeId());
        newsMapper.update(null, updateWrapper);
    }

    @Override
    public Integer countAuditedByNewsIds(List<Long> newsIds) {
        return newsMapper.selectCount(Wrappers.<News>lambdaQuery()
                .eq(News::getStatus, Constants.AuditFlag.AUDIT_PASSED)
                .in(News::getNewsId, newsIds));
    }
    @Override
    public void reloadNewsFromNewsWechat(Integer startId) {
        List<NewsWechat> newsWechats = newsWechatService.getNewNewsWechat(startId);
        if (CollectionUtil.isEmpty(newsWechats)) {
            return ;
        }
        // 转化成News对象
        for (NewsWechat newsWechat : newsWechats) {
            News news = new News();
            news.setUserId(18L);
            news.setNewsTypeId(null);
            news.setNewsTitle(newsWechat.getTitle());
            news.setType(2);
            news.setLink(newsWechat.getLink());
            news.setCreateTime(new Date(newsWechat.getSendtime() * 1000));
            news.setCreateBy(Constants.DEFAULT_USER);
            news.setStatus(0);
            news.setNewsImage(newsWechat.getCover());
            try {
                // 图片需要上传
                String filePath = FileUtil.downloadImage(newsWechat.getCover());
                news.setNewsImage(filePath);
            } catch (Exception e) {
                log.error("下载微信图片失败：", e.getMessage());
            }
            newsMapper.insert(news);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> saveNewsFromWechat() {
        List<Long> newsIds = new ArrayList<>();
        List<NewsWechat> newsWechats = newsWechatService.listWithoutRel();
        if (CollectionUtil.isEmpty(newsWechats)) {
            return newsIds;
        }
        // 转化成News对象
        for (NewsWechat newsWechat : newsWechats) {
            News news = new News();
            news.setUserId(18L);
            news.setNewsTypeId(null);
            news.setNewsTitle(newsWechat.getTitle());
            news.setType(2);
            news.setLink(newsWechat.getLink());
            news.setPublishTime(new Date(newsWechat.getSendtime() * 1000));
            news.setCreateTime(new Date());
            news.setCreateBy(Constants.DEFAULT_USER);
            news.setUpdateTime(new Date());
            news.setUpdateBy(Constants.DEFAULT_USER);
            news.setStatus(0);
            news.setToStatic(0);
            news.setNewsSource(Constants.NewsSource.WECHAT);
            news.setNewsImage(newsWechat.getCover());
            try {
                // 图片需要上传
                // 默认图片是300像素，尝试是否可以得到640像素的图片
                if (newsWechat.getCover().endsWith("/300")) {
                    int coverLength = newsWechat.getCover().length();
                    newsWechat.setCover(newsWechat.getCover().substring(0, coverLength - 4) + "/640");
                }
                String filePath = FileUtil.downloadImage(newsWechat.getCover());
                news.setNewsImage(filePath);
            } catch (Exception e) {
                log.error("下载微信图片失败：", e.getMessage());
            }
            newsMapper.insert(news);
            newsIds.add(news.getNewsId());

            // 成功了之后，插入关联
            NewsWechatRel newsWechatRel = new NewsWechatRel();
            newsWechatRel.setNewsWechatId(newsWechat.getId());
            newsWechatRel.setNewsId(news.getNewsId());
            newsWechatRel.setCreateTime(new Date());
            newsWechatRelService.save(newsWechatRel);
        }
        return newsIds;
    }


    @Override
    public List<News> listUnStatic() {
        return newsMapper.selectList(Wrappers.<News>lambdaQuery()
                .eq(News::getToStatic, 0));
    }

    @Override
    public void loadNewsToStatic() {
        // 获取所有未静态化的数据
        List<News> newsList = listUnStatic();
        if (CollectionUtil.isEmpty(newsList)) {
            return ;
        }
        for (News news : newsList) {
            if (news.getType() == 1) {

            } else {
                if (Constants.NewsSource.WECHAT.equals(news.getNewsSource())) {
                    String link = news.getLink()
                            .replaceAll("#wechat_redirect", "")
                            .replaceAll("#rd", "")
                            .replaceAll("http://", "https://");
                    // 去除chksm属性，不然无法拉去html
                    link = UrlUtil.removeParam(link, "chksm");
                    try {
                        String staticFilePath = saveUrl2Html(news.getNewsId(), link);
                        if (StringUtils.isNotEmpty(staticFilePath)) {
                            // 设置本地化标识
                            news.setToStatic(1);
                            news.setStaticFilePath(staticFilePath);
                            newsMapper.updateById(news);
                        }
                    } catch (Exception e) {
                        log.error("loadNewsToStatic error " + e.getMessage());
                    }
                }
            }
        }
    }


    /**
     * 转化成html
     * @param newsId
     * @param url
     * @throws Exception
     */
    private String saveUrl2Html(Long newsId, String url) throws Exception {
        String html = HttpUtil.get(url);
        List<FileDownloadDTO> fileList = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements linkElements = document.getElementsByTag("link");
        int icoNumber = 1;
        int imageNumber = 1;
        int cssNumber = 1;
        if (CollectionUtil.isNotEmpty(linkElements)) {
            Iterator<Element> iterator = linkElements.iterator();
            while (iterator.hasNext()) {
                Element linkElement = iterator.next();
                String href = linkElement.attr("href");
                boolean containFile = false;
                String fileType = null;
                String fileName = null;
                if (href.endsWith(".ico")) {
                    containFile = true;
                    fileType = "ico";
                    fileName = fileType + "_" + icoNumber + ".ico";
                    icoNumber ++;
                } else if (href.endsWith(".png")) {
                    containFile = true;
                    fileType = "image";
                    fileName =  fileType + "_" + imageNumber + ".png";
                    imageNumber ++;
                } else if (href.endsWith(".jpg")) {
                    containFile = true;
                    fileType = "image";
                    fileName =  fileType + "_" + imageNumber + ".jpg";
                    imageNumber ++;
                } else if (href.endsWith(".jpeg")) {
                    containFile = true;
                    fileType = "image";
                    fileName =  fileType + "_" + imageNumber + ".jpeg";
                    imageNumber ++;
                } else if (href.endsWith(".css")) {
                    containFile = true;
                    fileType = "css";
                    fileName =  fileType + "_" + cssNumber + ".css";
                    cssNumber ++;
                } else if (href.endsWith(".js")) {
                    // 删除
                    linkElement.remove();
                }

                if (containFile) {
                    FileDownloadDTO fileDownloadDTO = new FileDownloadDTO();
                    fileDownloadDTO.setUrl("https:" + href);
                    fileDownloadDTO.setType(fileType);
                    fileDownloadDTO.setName(fileName);
                    fileList.add(fileDownloadDTO);
                    linkElement.attr("href", "./" + fileType + "/" + fileName);
                }
            }
        }
        Elements imgElements = document.getElementsByTag("img");
        if (CollectionUtil.isNotEmpty(imgElements)) {
            String fileType = "image";
            for (Element imgElement : imgElements) {
                String dataSrc = imgElement.attr("data-src");
                if (StringUtils.isEmpty(dataSrc)) {
                    continue;
                }

                String dataType = imgElement.attr("data-type");
                String fileName = fileType + "_" + imageNumber + "." + dataType;
                imageNumber ++;
                FileDownloadDTO fileDownloadDTO = new FileDownloadDTO();
                fileDownloadDTO.setUrl(dataSrc);
                fileDownloadDTO.setType(fileType);
                fileDownloadDTO.setName(fileName);
                fileList.add(fileDownloadDTO);
                imgElement.attr("src", "./" + fileType + "/" + fileName);
                imgElement.removeAttr("data-src");
            }
        }

        // 浮动的微信公众号二维码
        Element qrCodeImgElement = document.getElementById("js_pc_qr_code_img");
        if (qrCodeImgElement != null) {
            // 使用标准图片
            qrCodeImgElement.attr("src", "../qrcode.bmp");
            // 不去掉id的话，会调用js主动生产src，覆盖配置的图片
            qrCodeImgElement.removeAttr("id");
        }

        // 保存静态资源
        saveStaticFile(newsId, fileList);

        String newHtml = document.html();
        System.out.println(newHtml);

        try {
            String defaultHtmlDownloadDir = FileUtil.getDefaultHtmlDownloadDir();
            // 文件名需要乱码，不然随便就能猜出访问路径
            String fileName = IdUtils.fastUUID() + ".html";
            File outfile = new File(defaultHtmlDownloadDir + "/" + newsId + "/" + fileName);
            FileUtil.createDir(outfile);
            FileWriter writer = new FileWriter(outfile);
            writer.write(newHtml);
            writer.close();
            return "/resource/html/" + newsId + "/" + fileName;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static void saveStaticFile(Long newsId, List<FileDownloadDTO> fileList) throws Exception {
        for (FileDownloadDTO singleFile : fileList) {
            String htmlDownloadDir = FileUtil.getDefaultHtmlDownloadDir();
            String relativeFileName = newsId + "/" + singleFile.getType() + "/" + singleFile.getName();
            singleFile.setDownloadFullPath(htmlDownloadDir + File.separator + relativeFileName);
            FileUtil.downloadFile(singleFile.getUrl(), singleFile.getDownloadFullPath());

        }
    }

    @Override
    public List<News> findNewsByIds(List<Long> newsIds) {
        return newsMapper.selectBatchIds(newsIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyToBack() {
        // 获取时间之后的所有新闻
        SysDictData syncDate = sysDictDataService.getOne(Wrappers.<SysDictData>lambdaQuery()
                .eq(SysDictData::getDictType, "SYNC_DATE"));
        String dictValue = syncDate.getDictValue();
        Date date = DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", dictValue);
        List<News> news = newsMapper.selectList(Wrappers.<News>lambdaQuery()
                .gt(News::getCreateTime, date));
        if (CollectionUtil.isNotEmpty(news)) {
            news.forEach(a -> {
                NewsTypeEnum enumType = NewsTypeEnum.getTableString(a.getNewsTypeId());
                String tableString = "NEWS_BAK.news_" + enumType.getTable();
                NewsBase newsBase = BeanUtil.copyProperties(a, NewsBase.class);
                newsBase.setNewsTypeName(enumType.getDesc());
                newsBase.setTypeName(1 == a.getType() ? "富文本" : "外链");
                newsBase.setSubjectName(getSubjectName(a.getSubject()));
                newsBase.setStatusName(getStatusName(a.getStatus()));
                saveNewsBase(tableString, newsBase);
            });
            syncDate.setDictValue(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            sysDictDataService.updateById(syncDate);
        }
        return true;
    }

    private void saveNewsBase(String tableString, NewsBase news) {
        try {
            newsMapper.saveNewsBase(tableString, news);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String getSubjectName(Integer subject) {
        if (subject == null) {
            return "未定义";
        }
        if (subject == 0) {
            return "组工锐评";
        }
        if (subject == 1) {
            return "人才增值服务";
        }
        if (subject == 2) {
            return "“天天有感”党群服务中心展播";
        }
        if (subject == 3) {
            return "和美争创 三百计划";
        }
        if (subject == 4) {
            return "担当作为好干部风采录";
        }
        return "未定义";
    }

    private String getStatusName(Integer status) {
        if (status == null) {
            return "审核中";
        }
        if (status == 1) {
            return "审核成功";
        }
        if (status == 2) {
            return "审核失败";
        }
        return "审核中";
    }

    @Autowired
    private ISlaveService slaveService;

    @Override
    public boolean copyToBm() {
        // 获取时间之后的所有新闻
        SysDictData syncDate = sysDictDataService.getOne(Wrappers.<SysDictData>lambdaQuery()
                .eq(SysDictData::getDictType, "BM_SYNC_DATE"));
        String dictValue = syncDate.getDictValue();
        Date date = DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", dictValue);
        List<News> news = newsMapper.selectList(Wrappers.<News>lambdaQuery()
                .gt(News::getCreateTime, date));
        if (CollectionUtil.isNotEmpty(news)) {
            news.forEach(a -> {
                NewsTypeEnum enumType = NewsTypeEnum.getTableString(a.getNewsTypeId());
                if (!enumType.equals(NewsTypeEnum.COMMON)) {
                    String tableString = enumType.getZfTable();
                    NewsBase newsBase = BeanUtil.copyProperties(a, NewsBase.class);
                    newsBase.setNewsTypeName(enumType.getDesc());
                    newsBase.setTypeName(1 == a.getType() ? "富文本" : "外链");
                    newsBase.setSubjectName(getSubjectName(a.getSubject()));
                    newsBase.setStatusName(getStatusName(a.getStatus()));
                    if (newsBase.getNewsContent() == null) {
                        newsBase.setNewsContent("");
                    }
                    slaveService.saveNewsBaseBm(tableString, newsBase);
                }
            });
            syncDate.setDictValue(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            sysDictDataService.updateById(syncDate);
        }
        return true;
    }

}
