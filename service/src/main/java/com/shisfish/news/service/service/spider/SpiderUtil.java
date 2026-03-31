package com.shisfish.news.service.service.spider;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shisfish.news.common.constant.WechatConstant;
import com.shisfish.news.common.utils.StringUtil;
import com.shisfish.news.dao.domain.biz.NewsWechat;
import com.shisfish.news.dao.plainordinaryjavaobject.biz.WechatColumn;
import com.shisfish.news.service.service.forest.GatewayClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shisfish
 * @date 2023/8/21
 */

@Component
public class SpiderUtil {

    private static final Pattern ARTICLE_URL_PATTERN = Pattern.compile("https://mp\\.weixin\\.qq\\.com/s\\?[^\\\"'\\s<]+", Pattern.CASE_INSENSITIVE);
    private static final Pattern IMAGE_URL_PATTERN = Pattern.compile("https://mmbiz\\.qpic\\.cn/[^\\\"'\\s<]+", Pattern.CASE_INSENSITIVE);
    private static final Pattern PUBLISH_TIME_PATTERN = Pattern.compile("var\\s+publish_time\\s*=\\s*['\"]?(\\d{10})['\"]?");
    private static final Pattern MSG_CDN_URL_PATTERN = Pattern.compile("var\\s+msg_cdn_url\\s*=\\s*['\"]([^'\"]+)['\"]");
    private static final Pattern OG_IMAGE_PATTERN = Pattern.compile("<meta[^>]+property=['\"]og:image['\"][^>]+content=['\"]([^'\"]+)['\"]", Pattern.CASE_INSENSITIVE);

    @Autowired
    private GatewayClient client;

    private static GatewayClient gatewayClient;

    @PostConstruct
    public void init() {
        gatewayClient = client;
    }

    public static List<NewsWechat> getArticlesHomepage(WechatColumn column) {
        if (WechatConstant.TYPE_HOMEPAGE.equals(column.getType())) {
            JSONArray articles = getArticlesHomepage(column.getSn());
            return getFromHomepage(articles);
        } else if (WechatConstant.TYPE_APPMSGALBUM.equals(column.getType())) {
            JSONArray articles = getArticlesAalbum(column.getAlbumId());
            return getFromAlbum(articles);
        } else {
            return new ArrayList<>();
        }
    }

    public static List<NewsWechat> getFromHomepage(JSONArray jsonArray) {
        // {"aid":"2247728258_2","title":"助企·纾困 | 惠企小贴士第二弹来了！","cover":"http:\\/\\/mmbiz.qpic.cn\\/mmbiz_jpg\\/6QkdLJfXdGwdIFKzAAnAcLIj3LCAzsGtbVI97b1616wJjaakjFTJ7NwnVr6viagzZcrowKJhA4a6ja8e4ajYrUw\\/0","link":"http:\\/\\/mp.weixin.qq.com\\/s?__biz=MzIxNDUyMzcwOA==&mid=2247728258&idx=2&sn=3053adbec73e6203679f4f27270e3257&scene=19#wechat_redirect","digest":"","appmsgid":2247728258,"itemidx":2,"type":9,"item_show_type":0,"copyright_stat":100,"author":"","sendtime":1656067388}
        List<NewsWechat> list = new ArrayList<>();
        if (CollectionUtil.isEmpty(jsonArray)) {
            return list;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            NewsWechat newsWechat = new NewsWechat();
            JSONObject article = jsonArray.getJSONObject(i);
            newsWechat.setLink(article.getString("link"));
            newsWechat.setCover(article.getString("cover"));
            newsWechat.setSendtime(article.getLong("sendtime"));
            newsWechat.setTitle(article.getString("title"));
            newsWechat.setAppmsgid(article.getString("appmsgid"));
            newsWechat.setAid(article.getString("aid"));
            list.add(newsWechat);
        }
        return list;
    }

    public static List<NewsWechat> getFromAlbum(JSONArray jsonArray) {
        // {"is_pay_subscribe":"0","is_read":"0","create_time":"1689487830","user_read_status":"0","itemidx":"1","tts_is_ban":"0","msgid":"2247818285","cover_img_1_1":"https://mmbiz.qpic.cn/sz_mmbiz_jpg/6QkdLJfXdGzC5icXSRJnNmwBSmQR0DibFvia5ZU2niaSeQHqktZicmt7v4zmM6mUsdyyYdgSAicAPtZT5GdjouDcYniag/300","title":"组工锐评 | “后浪”村社书记的“三笔帐”","key":"3214523708_2247818285_1","url":"http://mp.weixin.qq.com/s?__biz=MzIxNDUyMzcwOA==&mid=2247818285&idx=1&sn=a06d8f4017cace20ede9febea690348f&chksm=97a8167ea0df9f683fe18cd73ccd9093ebf7719fa4ea37a735d17f0067965cf3c14cebe2f50c#rd"}
        List<NewsWechat> list = new ArrayList<>();
        if (CollectionUtil.isEmpty(jsonArray)) {
            return list;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            NewsWechat newsWechat = new NewsWechat();
            JSONObject article = jsonArray.getJSONObject(i);
            newsWechat.setLink(article.getString("url"));
            newsWechat.setCover(article.getString("cover_img_1_1"));
            newsWechat.setSendtime(article.getLong("create_time"));
            newsWechat.setTitle(article.getString("title"));
            newsWechat.setAppmsgid(article.getString("msgid"));
            newsWechat.setAid(article.getString("key"));
            list.add(newsWechat);
        }
        return list;
    }

    public static JSONArray getArticlesHomepage(String sn) {
        Map<String, Object> params = new HashMap<>();
        params.put("__biz", WechatConstant.SHAOXINGZUGONG);
        params.put("hid", "22");
        params.put("sn", sn);
        params.put("f", "json");
        params.put("begin", 0);
        params.put("count", 5);
        params.put("action", "appmsg_list");

        String response = gatewayClient.doPost(WechatConstant.ARTICLE_HOMEPAGE_URL, params, new HashMap<>());
        System.out.println("official account get response : " + response);
        JSONObject responseObject = JSON.parseObject(response);
        // get请求，数据格式：response.data.homepage_render.appmsg_list
        // post请求，数据格式：response.appmsg_list
        // JSONObject data = responseObject.getJSONObject("data");
        // JSONObject homepageRender = data.getJSONObject("homepage_render");
        return responseObject.getJSONArray("appmsg_list");
    }

    public static JSONArray getArticlesAalbum(String albumId) {
        Map<String, Object> params = new HashMap<>();
        params.put("__biz", WechatConstant.SHAOXINGZUGONG);
        params.put("hid", "22");
        params.put("album_id", albumId);
        params.put("f", "json");
        params.put("count", 20);
        params.put("action", "getalbum");

        String response = gatewayClient.doGet(WechatConstant.ARTICLE_ALBUM_URL, params, new HashMap<>());
        System.out.println("official account get response : " + response);
        JSONObject responseObject = JSON.parseObject(response);
        JSONObject getalbumResp = responseObject.getJSONObject("getalbum_resp");
        if (getalbumResp == null) {
            return null;
        }
        return getalbumResp.getJSONArray("article_list");
    }

    public static List<NewsWechat> getArticlesHomepage(String fakeid, String token, String cookie, Integer begin) {
        JSONArray articles = getArticleApp(fakeid, token, cookie, begin);
        return getFromApp(articles);
    }

    public static List<NewsWechat> searchShaoxingZugongArticles() {
        return searchArticlesByAccountName("绍兴组工");
    }

    public static List<NewsWechat> searchArticlesByAccountName(String accountName) {
        List<NewsWechat> list = new ArrayList<>();
        if (StrUtil.isBlank(accountName)) {
            return list;
        }
        String searchUrl = "https://weixin.sogou.com/weixin?type=1&query=" + accountName;
        String searchHtml = fetchSearchPage(searchUrl);
        if (StrUtil.isBlank(searchHtml)) {
            return list;
        }
        String profileUrl = extractSogouProfileUrl(searchHtml, accountName);
        if (StrUtil.isBlank(profileUrl)) {
            return list;
        }
        String profileHtml = fetchSearchPage(profileUrl);
        JSONArray articles = parseSogouArticles(profileHtml);
        return getFromApp(articles);
    }

    public static List<NewsWechat> getFromApp(JSONArray jsonArray) {
        List<NewsWechat> list = new ArrayList<>();
        if (CollectionUtil.isEmpty(jsonArray)) {
            return list;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            NewsWechat newsWechat = new NewsWechat();
            JSONObject article = jsonArray.getJSONObject(i);
            newsWechat.setLink(article.getString("link").replace("\\\\\\\\\\\\", ""));
            newsWechat.setCover(article.getString("cover").replace("\\\\\\\\\\\\", ""));
            newsWechat.setSendtime(article.getLong("update_time"));
            newsWechat.setTitle(article.getString("title"));
            newsWechat.setAppmsgid(article.getString("appmsgid"));
            newsWechat.setAid(article.getString("aid"));
            list.add(newsWechat);
        }
        return list;
    }

    public static List<NewsWechat> getFromApp1(JSONArray jsonArray) {
        List<NewsWechat> list = new ArrayList<>();
        if (CollectionUtil.isEmpty(jsonArray)) {
            return list;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            NewsWechat newsWechat = new NewsWechat();
            JSONObject article = jsonArray.getJSONObject(i);
            newsWechat.setLink(article.getString("link"));
            newsWechat.setCover(article.getString("cover"));
            newsWechat.setSendtime(article.getLong("update_time"));
            newsWechat.setTitle(article.getString("title"));
            newsWechat.setAppmsgid(article.getString("appmsgid"));
            newsWechat.setAid(article.getString("aid"));
            list.add(newsWechat);
        }
        return list;
    }

    public static JSONArray getArticleApp(String fakeid, String token, String cookie, Integer begin) {
//        JSONArray articles = getArticleFromPublicPages(cookie, begin);
//        if (CollectionUtil.isNotEmpty(articles)) {
//            return articles;
//        }

        Map<String, Object> params = new HashMap<>();
        params.put("sub", "list");
        params.put("search_field", null);
        params.put("begin", begin);
        // 最近3天的，如果一天发表了10条文章，则返回10条数据
        params.put("count", "10");
        params.put("fakeid", fakeid);
        params.put("type", "101_1");
        params.put("query", "");
        params.put("free_publish_type", "1");
        params.put("sub_action", "list_ex");
        params.put("fingerprint", "e94f7cccd784fc6ac9a553628982ad19");
        params.put("token", token);
        params.put("lang", "zh_CN");
        params.put("f", "json");
        params.put("ajax", "1");

        Map<String, Object> headers = new HashMap<>();
        headers.put("Cookie", cookie);
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36 QuarkPC/6.6.0.776");

        String response = gatewayClient.doGet(WechatConstant.ARTICAL_APP_URL, params, headers);
        System.out.println("official account get response : " + response);
        JSONObject jsonObject = JSON.parseObject(response);
        String publishPage = jsonObject.getString("publish_page");
        JSONArray getList = new JSONArray();
        if (StringUtil.isNoneBlank(publishPage)) {
            JSONObject publicData = JSONArray.parseObject(publishPage);
            JSONArray publishList = publicData.getJSONArray("publish_list");
            if (CollectionUtil.isNotEmpty(publishList)) {
                publishList.forEach(singleData -> {
                    JSONObject singleDataObject = (JSONObject) singleData;
                    String publishInfo = singleDataObject.getString("publish_info");
                    JSONObject singleInfo = JSONObject.parseObject(publishInfo);
                    JSONArray appmsgex = singleInfo.getJSONArray("appmsgex");
                    getList.addAll(appmsgex);
                });
            }
        }
        return getList;
    }

    public static JSONArray getArticleApp1(String fakeid, String token, String cookie, Integer begin) {
//        JSONArray articles = getArticleFromPublicPages(cookie, begin);
//        if (CollectionUtil.isNotEmpty(articles)) {
//            return articles;
//        }

        Map<String, Object> params = new HashMap<>();
        params.put("action", "list_ex");
        params.put("begin", begin);
        // 最近3天的，如果一天发表了10条文章，则返回10条数据
        params.put("count", "10");
        params.put("fakeid", fakeid);
        params.put("type", "9");
        params.put("query", "");
        params.put("token", token);
        params.put("lang", "zh_CN");
        params.put("f", "json");
        params.put("ajax", "1");

        Map<String, Object> headers = new HashMap<>();
        headers.put("Cookie", cookie);
        headers.put("User-Agent", "PostmanRuntime/7.28.0");

        String response = gatewayClient.doGet(WechatConstant.ARTICAL_APP_URL, params, headers);
        System.out.println("official account get response : " + response);
        JSONObject jsonObject = JSON.parseObject(response);
        return jsonObject.getJSONArray("app_msg_list");
    }

    private static JSONArray getArticleFromPublicPages(String cookie, Integer begin) {
        if (StrUtil.isBlank(cookie)) {
            return new JSONArray();
        }
        String userName = extractCookieValue(cookie, "slave_user");
        if (StrUtil.isBlank(userName)) {
            return new JSONArray();
        }

        String profileUrl = "https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz="
                + userName
                + "&scene=124#wechat_redirect";
        String html = fetchWechatPage(profileUrl, cookie);
        JSONArray articles = parseProfileArticles(html);
        if (CollectionUtil.isEmpty(articles)) {
            return new JSONArray();
        }
        return sliceArticles(articles, begin, 10);
    }

    private static JSONArray parseProfileArticles(String html) {
        JSONArray results = new JSONArray();
        if (StrUtil.isBlank(html)) {
            return results;
        }
        Document document = Jsoup.parse(html);
        Elements links = document.select("a[href*=mp.weixin.qq.com/s?], a[href*=__biz=]");
        Map<String, JSONObject> uniqueMap = new LinkedHashMap<>();
        for (Element linkElement : links) {
            String link = normalizeUrl(linkElement.attr("abs:href"));
            if (StrUtil.isBlank(link)) {
                link = normalizeUrl(linkElement.attr("href"));
            }
            if (StrUtil.isBlank(link) || uniqueMap.containsKey(link)) {
                continue;
            }
            JSONObject article = buildArticleFromElement(linkElement, link);
            if (article != null) {
                uniqueMap.put(link, article);
            }
        }

        if (uniqueMap.isEmpty()) {
            Matcher matcher = ARTICLE_URL_PATTERN.matcher(html);
            while (matcher.find()) {
                String link = normalizeUrl(matcher.group());
                if (StrUtil.isBlank(link) || uniqueMap.containsKey(link)) {
                    continue;
                }
                JSONObject article = enrichArticleByDetailPage(link, html, null, findCoverFromHtml(html), findPublishTime(html));
                if (article != null) {
                    uniqueMap.put(link, article);
                }
            }
        }
        results.addAll(uniqueMap.values());
        return results;
    }

    private static JSONArray parseSogouArticles(String html) {
        JSONArray results = new JSONArray();
        if (StrUtil.isBlank(html)) {
            return results;
        }
        Document document = Jsoup.parse(html);
        Elements articleNodes = document.select("li, .news-box li, .txt-box");
        Map<String, JSONObject> uniqueMap = new LinkedHashMap<>();
        for (Element node : articleNodes) {
            Element linkElement = node.select("a[href]").first();
            if (linkElement == null) {
                continue;
            }
            String link = normalizeUrl(linkElement.attr("abs:href"));
            if (StrUtil.isBlank(link)) {
                link = normalizeUrl(linkElement.attr("href"));
            }
            if (StrUtil.isBlank(link) || uniqueMap.containsKey(link) || !link.contains("mp.weixin.qq.com")) {
                continue;
            }
            String title = cleanText(linkElement.text());
            String cover = findCoverFromElement(node);
            Long updateTime = parseSogouTime(node.text());
            JSONObject article = enrichArticleByDetailPage(link, null, title, cover, updateTime);
            if (article != null) {
                uniqueMap.put(link, article);
            }
        }
        results.addAll(uniqueMap.values());
        return results;
    }

    private static JSONObject buildArticleFromElement(Element element, String link) {
        String title = cleanText(element.text());
        String cover = findCoverFromElement(element);
        Long updateTime = findPublishTime(element.outerHtml());
        if (StrUtil.isBlank(title) || updateTime == null || StrUtil.isBlank(cover)) {
            return enrichArticleByDetailPage(link, null, title, cover, updateTime);
        }
        return buildArticleJson(link, title, cover, updateTime);
    }

    private static JSONObject enrichArticleByDetailPage(String link, String html, String title, String cover, Long updateTime) {
        String targetHtml = html;
        if (StrUtil.isBlank(targetHtml)) {
            targetHtml = fetchWechatPage(link, null);
        }
        if (StrUtil.isBlank(targetHtml)) {
            return buildArticleJson(link, title, cover, updateTime);
        }
        Document document = Jsoup.parse(targetHtml);
        String finalTitle = firstNonBlank(title,
                cleanText(document.select("meta[property=og:title]").attr("content")),
                cleanText(document.select("#activity-name").text()),
                cleanText(document.title()));
        String finalCover = firstNonBlank(cover,
                cleanText(document.select("meta[property=og:image]").attr("content")),
                findCoverFromHtml(targetHtml));
        Long finalTime = updateTime != null ? updateTime : findPublishTime(targetHtml);
        return buildArticleJson(link, finalTitle, finalCover, finalTime);
    }

    private static JSONObject buildArticleJson(String link, String title, String cover, Long updateTime) {
        if (StrUtil.isBlank(link) || StrUtil.isBlank(title)) {
            return null;
        }
        JSONObject article = new JSONObject();
        article.put("link", link);
        article.put("title", title);
        article.put("cover", cover);
        article.put("update_time", updateTime);
        String mid = extractQueryParam(link, "mid");
        String idx = extractQueryParam(link, "idx");
        article.put("appmsgid", mid);
        article.put("aid", StrUtil.isAllNotBlank(mid, idx) ? mid + "_" + idx : link);
        return article;
    }

    private static JSONArray sliceArticles(JSONArray articles, Integer begin, int count) {
        JSONArray results = new JSONArray();
        if (CollectionUtil.isEmpty(articles)) {
            return results;
        }
        int safeBegin = begin == null ? 0 : Math.max(begin, 0);
        int end = Math.min(safeBegin + count, articles.size());
        for (int i = safeBegin; i < end; i++) {
            results.add(articles.getJSONObject(i));
        }
        return results;
    }

    private static String fetchWechatPage(String url, String cookie) {
        try {
            HttpRequest request = HttpUtil.createGet(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
                    .header("Accept-Language", "zh-CN,zh;q=0.9")
                    .timeout(15000);
            if (StrUtil.isNotBlank(cookie)) {
                request.header("Cookie", cookie);
            }
            HttpResponse response = request.execute();
            if (response.isOk()) {
                return response.body();
            }
        } catch (Exception e) {
            System.out.println("fetch wechat page failed: " + e.getMessage());
        }
        return null;
    }

    private static String fetchSearchPage(String url) {
        try {
            HttpResponse response = HttpUtil.createGet(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
                    .header("Accept-Language", "zh-CN,zh;q=0.9")
                    .header("Referer", "https://weixin.sogou.com/")
                    .timeout(15000)
                    .execute();
            if (response.isOk()) {
                return response.body();
            }
        } catch (Exception e) {
            System.out.println("fetch sogou page failed: " + e.getMessage());
        }
        return null;
    }

    private static void normalizeWechatNews(NewsWechat newsWechat) {
        if (newsWechat == null) {
            return;
        }
        newsWechat.setLink(normalizeUrl(newsWechat.getLink()));
        newsWechat.setCover(normalizeUrl(newsWechat.getCover()));
        if (StrUtil.isBlank(newsWechat.getAppmsgid())) {
            newsWechat.setAppmsgid(extractQueryParam(newsWechat.getLink(), "mid"));
        }
        if (StrUtil.isBlank(newsWechat.getAid())) {
            String mid = extractQueryParam(newsWechat.getLink(), "mid");
            String idx = extractQueryParam(newsWechat.getLink(), "idx");
            newsWechat.setAid(StrUtil.isAllNotBlank(mid, idx) ? mid + "_" + idx : newsWechat.getLink());
        }
        if (newsWechat.getSendtime() == null || newsWechat.getSendtime() <= 0) {
            newsWechat.setSendtime(System.currentTimeMillis() / 1000);
        }
    }

    private static String findCoverFromElement(Element element) {
        if (element == null) {
            return null;
        }
        Elements images = element.select("img[src], img[data-src], img[data-url]");
        Element image = images.isEmpty() ? null : images.first();
        if (image != null) {
            return normalizeUrl(firstNonBlank(image.attr("abs:src"), image.attr("src"), image.attr("data-src"), image.attr("data-url")));
        }
        return findCoverFromHtml(element.outerHtml());
    }

    private static String findCoverFromHtml(String html) {
        if (StrUtil.isBlank(html)) {
            return null;
        }
        Matcher imageMatcher = IMAGE_URL_PATTERN.matcher(html);
        if (imageMatcher.find()) {
            return normalizeUrl(imageMatcher.group());
        }
        Matcher msgCdnMatcher = MSG_CDN_URL_PATTERN.matcher(html);
        if (msgCdnMatcher.find()) {
            return normalizeUrl(msgCdnMatcher.group(1));
        }
        Matcher ogImageMatcher = OG_IMAGE_PATTERN.matcher(html);
        if (ogImageMatcher.find()) {
            return normalizeUrl(ogImageMatcher.group(1));
        }
        return null;
    }

    private static Long findPublishTime(String html) {
        if (StrUtil.isBlank(html)) {
            return null;
        }
        Matcher matcher = PUBLISH_TIME_PATTERN.matcher(html);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        return null;
    }

    private static String normalizeUrl(String url) {
        if (StrUtil.isBlank(url)) {
            return null;
        }
        String normalized = url.replace("\\/", "/").replace("&amp;", "&").trim();
        if (normalized.startsWith("//")) {
            return "https:" + normalized;
        }
        if (normalized.startsWith("http://mp.weixin.qq.com")) {
            return normalized.replace("http://", "https://");
        }
        return normalized;
    }

    private static String extractQueryParam(String url, String key) {
        if (StrUtil.isBlank(url) || StrUtil.isBlank(key)) {
            return null;
        }
        String query = StrUtil.subAfter(url, "?", false);
        if (StrUtil.isBlank(query)) {
            return null;
        }
        query = StrUtil.subBefore(query, "#", false);
        for (String pair : query.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2 && key.equals(parts[0])) {
                return parts[1];
            }
        }
        return null;
    }

    private static String extractCookieValue(String cookie, String key) {
        if (StrUtil.isBlank(cookie) || StrUtil.isBlank(key)) {
            return null;
        }
        for (String item : cookie.split(";")) {
            String[] parts = item.trim().split("=", 2);
            if (parts.length == 2 && key.equals(parts[0].trim())) {
                return parts[1].trim();
            }
        }
        return null;
    }

    private static String extractSogouProfileUrl(String html, String accountName) {
        if (StrUtil.isBlank(html)) {
            return null;
        }
        Document document = Jsoup.parse(html);
        Elements links = document.select("a[href]");
        for (Element link : links) {
            String href = normalizeUrl(link.attr("abs:href"));
            if (StrUtil.isBlank(href)) {
                href = normalizeUrl(link.attr("href"));
            }
            String text = cleanText(link.text());
            if (StrUtil.isBlank(href) || StrUtil.isBlank(text)) {
                continue;
            }
            if (text.contains(accountName) && (href.contains("profile") || href.contains("weixin?type=1"))) {
                return href;
            }
        }
        return null;
    }

    private static Long parseSogouTime(String text) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        Matcher matcher = Pattern.compile("(20\\d{2}-\\d{2}-\\d{2})").matcher(text);
        if (!matcher.find()) {
            return null;
        }
        try {
            return cn.hutool.core.date.DateUtil.parseDate(matcher.group(1)).getTime() / 1000;
        } catch (Exception e) {
            return null;
        }
    }

    private static String cleanText(String text) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        String cleaned = text.replace("\u00A0", " ").replaceAll("\\s+", " ").trim();
        return StrUtil.emptyToNull(cleaned);
    }

    private static String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (StrUtil.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }
}
