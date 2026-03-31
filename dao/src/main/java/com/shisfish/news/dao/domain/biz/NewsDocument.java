package com.shisfish.news.dao.domain.biz;

import com.shisfish.news.dao.domain.system.SysUser;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 全局检索新闻
 * type = FieldType.Text 类型 Text支持分词
 * analyzer = "ik_smart" 创建索引分词器
 * index = true 添加数据的时候 是否分词
 * store = flase 是否存储
 * Keyword 不分词 分词类型
 * indexName 索引库名称
 * type 类型名
 * shards 分片
 * @Version: 1.0.0
 */
@ToString
//@Document(indexName = "news", type = "news", shards = 1, replicas = 0)
public class NewsDocument implements Serializable {

    private static final long serialVersionUID = 6042589917577710313L;
    /**
     * 新闻id
     */
    @Id
//    @Field(type = FieldType.Long)
    private Long newsId;
    /**
     * 作者
     */
    private SysUser author;
    /**
     * 新闻栏目id
     */
    private NewsType newsType;

    /**
     * 新闻标题
     */
//    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String newsTitle;

    /**
     * 新闻内容
     */
//    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String newsContent;

    /**
     * 新闻属性 0.头条区新闻 1.幻灯片区新闻 2.热点区新闻
     * 这里是文字
     */
//    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String newsAttr;

    /**
     * 新闻封面
     */
    private String newsImage;


    /**
     * 新闻点赞数
     */
    private Long thumbs;

    /**
     * 新闻浏览量
     */
    private Long visits;

    /**
     * 新闻评论数
     */
    private Long comments;

    /**
     * 新闻收藏数
     */
    private Long collects;
    /**
     * 新闻来源
     */
    private String newsSource;
    /**
     * 新闻来源原创作者(博客使用)
     */
//    @Field(type = FieldType.Keyword)
    private String newsSourceAuthor;

    /**
     * 新闻博客分类标签
     */
//    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String newsSourceTags;

    /**
     * 创建者
     */
//    @Field(type = FieldType.Keyword)
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Long getNewsId() {
        return newsId;
    }

    public NewsDocument setNewsId(Long newsId) {
        this.newsId = newsId;
        return this;
    }

    public SysUser getAuthor() {
        return author;
    }

    public NewsDocument setAuthor(SysUser author) {
        this.author = author;
        return this;
    }

    public NewsType getNewsType() {
        return newsType;
    }

    public NewsDocument setNewsType(NewsType newsType) {
        this.newsType = newsType;
        return this;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public NewsDocument setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
        return this;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public NewsDocument setNewsContent(String newsContent) {
        this.newsContent = newsContent;
        return this;
    }

    public String getNewsAttr() {
        return newsAttr;
    }

    public NewsDocument setNewsAttr(String newsAttr) {
        this.newsAttr = newsAttr;
        return this;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public NewsDocument setNewsImage(String newsImage) {
        this.newsImage = newsImage;
        return this;
    }


    public Long getThumbs() {
        return thumbs;
    }

    public NewsDocument setThumbs(Long thumbs) {
        this.thumbs = thumbs;
        return this;
    }

    public Long getVisits() {
        return visits;
    }

    public NewsDocument setVisits(Long visits) {
        this.visits = visits;
        return this;
    }

    public Long getComments() {
        return comments;
    }

    public NewsDocument setComments(Long comments) {
        this.comments = comments;
        return this;
    }

    public Long getCollects() {
        return collects;
    }

    public NewsDocument setCollects(Long collects) {
        this.collects = collects;
        return this;
    }

    public String getNewsSource() {
        return newsSource;
    }

    public NewsDocument setNewsSource(String newsSource) {
        this.newsSource = newsSource;
        return this;
    }

    public String getNewsSourceAuthor() {
        return newsSourceAuthor;
    }

    public NewsDocument setNewsSourceAuthor(String newsSourceAuthor) {
        this.newsSourceAuthor = newsSourceAuthor;
        return this;
    }

    public String getNewsSourceTags() {
        return newsSourceTags;
    }

    public NewsDocument setNewsSourceTags(String newsSourceTags) {
        this.newsSourceTags = newsSourceTags;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public NewsDocument setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public NewsDocument setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
}
