package com.shisfish.news.service.service.spider;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shisfish.news.common.constant.WechatConstant;
import com.shisfish.news.dao.plainordinaryjavaobject.biz.WechatColumn;
import com.shisfish.news.dao.domain.biz.NewsWechat;
import com.shisfish.news.service.service.forest.GatewayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shisfish
 * @date 2023/8/21
 */

@Component
public class SpiderUtil {

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
        } else if (WechatConstant.TYPE_APPMSGALBUM.equals(column.getType())){
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

    public static List<NewsWechat> getFromApp(JSONArray jsonArray) {
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

}
