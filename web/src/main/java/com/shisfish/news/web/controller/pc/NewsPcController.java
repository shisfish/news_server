package com.shisfish.news.web.controller.pc;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shisfish.news.common.constant.Constants;
import com.shisfish.news.common.constant.UserConstants;
import com.shisfish.news.common.core.controller.BaseController;
import com.shisfish.news.dao.datatransferobject.biz.NewsWechatDTO;
import com.shisfish.news.common.properties.WechatProperty;
import com.shisfish.news.common.result.PageResult;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.common.utils.file.FileUploadUtils;
import com.shisfish.news.common.utils.file.FileUtil;
import com.shisfish.news.common.utils.file.ZipUtil;
import com.shisfish.news.common.utils.string.StringUtils;
import com.shisfish.news.dao.domain.biz.Banner;
import com.shisfish.news.dao.domain.biz.News;
import com.shisfish.news.dao.domain.biz.NewsType;
import com.shisfish.news.dao.domain.biz.NewsWechat;
import com.shisfish.news.dao.domain.biz.NewsWechatRel;
import com.shisfish.news.service.service.biz.IBannerService;
import com.shisfish.news.service.service.biz.INewsService;
import com.shisfish.news.service.service.biz.INewsTypeService;
import com.shisfish.news.service.service.biz.INewsWechatRelService;
import com.shisfish.news.service.service.biz.INewsWechatService;
import com.shisfish.news.service.service.forest.GatewayClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */
@Api(tags = "PC接口")
@RestController
@RequestMapping("/news/pc")
public class NewsPcController extends BaseController {

    @Autowired
    private INewsTypeService newsTypeService;

    @Autowired
    private INewsService newsService;

    @Autowired
    private IBannerService bannerService;

    @Autowired
    private INewsWechatService newsWechatService;

    @Autowired
    private INewsWechatRelService newsWechatRelService;

    @Autowired
    private GatewayClient gatewayClient;

    @Value(value = "${loadnews.url}")
    private String loadnewsUrl;

    @Value(value = "${loadnews.fileName}")
    private String loadnewsFileName;

    @Value(value = "${loadnews.updateFileApi}")
    private String loadnewsUpdateFileApi;

    /**
     * 获取所有新闻栏目下拉列表
     */
    @ApiOperation(value = "获取所有新闻栏目列表", notes = "获取所有新闻栏目列表")
    @GetMapping("/listType")
    public Result<List<NewsType>> listType() {
        return ResultUtils.success(newsTypeService.list(new LambdaQueryWrapper<NewsType>()
                .select(NewsType::getNewsTypeId, NewsType::getNewsTypeName, NewsType::getStatus)
                .eq(NewsType::getDelFlag, UserConstants.NORMAL)
                .orderByAsc(NewsType::getOrderNum)
                .orderByAsc(NewsType::getNewsTypeId)
        ));
    }

    @ApiOperation(value = "条件分页查询Banner列表", notes = "条件分页查询新闻Banner详情")
    @GetMapping("/listBanner/{page}/{size}")
    public PageResult<List<Banner>> listBanner(@PathVariable("page") int page, @PathVariable("size") int size) {
        Banner banner = new Banner();
        // 新闻只查询审核通过的
        banner.setStatus(Constants.AuditFlag.AUDIT_PASSED);
        IPage<Banner> iPage = bannerService.findPage(new Page<>(page, size), banner);
        return ResultUtils.success(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), iPage.getRecords());
    }

    @ApiOperation(value = "条件分页查询新闻列表", notes = "条件分页查询新闻列表详情")
    @GetMapping("/listNews/{page}/{size}")
    public PageResult<List<News>> listNews(@PathVariable("page") int page, @PathVariable("size") int size, Long newsTypeId, Integer subject) {
        News news = new News();
        news.setNewsTypeId(newsTypeId);
        news.setSubject(subject);
        // 新闻只查询审核通过的
        news.setStatus(Constants.AuditFlag.AUDIT_PASSED);
        IPage<News> iPage = newsService.findPageAll(new Page<>(page, size), news);
        return ResultUtils.success(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), iPage.getRecords());
    }

    @ApiOperation(value = "条件分页查询新闻列表", notes = "条件分页查询新闻列表详情")
    @GetMapping("/listNewsWithImage/{page}/{size}")
    public PageResult<List<News>> listNewsWithImage(@PathVariable("page") int page, @PathVariable("size") int size, Long newsTypeId) {
        News news = new News();
        if (newsTypeId == null) {
            newsTypeId = 1L;
        }
        news.setNewsTypeId(newsTypeId);
        // 新闻只查询审核通过的
        news.setStatus(Constants.AuditFlag.AUDIT_PASSED);
        IPage<News> iPage = newsService.findPageWithImage(new Page<>(page, size), news);
        return ResultUtils.success(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), iPage.getRecords());
    }


    @ApiOperation(value = "获取新闻详细信息", notes = "获取新闻详细信息详情")
    @GetMapping(value = "/news/{newsId}")
    public Result<News> getInfo(@PathVariable("newsId") Long newsId) {
        return ResultUtils.success(newsService.findNewsById(newsId));
    }

    @Autowired
    private WechatProperty wechatProperty;

    @ApiOperation(value = "获取新闻详细信息", notes = "获取新闻详细信息详情")
    @GetMapping(value = "/saveNewsBySpider")
    public Result saveNewsBySpider() {
        newsService.saveNewsBySpider();
        return ResultUtils.success();
    }

    @ApiOperation(value = "获取新闻详细信息", notes = "获取新闻详细信息详情")
    @GetMapping(value = "/reloadNewsFromNewsWechat")
    public Result reloadNewsFromNewsWechat(Integer startId) {
        newsService.reloadNewsFromNewsWechat(startId);
        return ResultUtils.success();
    }

    @ApiOperation(value = "通用上传", notes = "通用上传详情")
    @PostMapping("/downloadFile")
    public Result downloadFile(String fileUrl) throws Exception {
        String filePath = FileUtil.downloadImage(fileUrl);
        String url = "http://localhost:9000" + filePath;
        return ResultUtils.success(url);
    }
    @ApiOperation(value = "按类型下载", notes = "通用上传详情")
    @GetMapping("/downloadHtmlImage")
    public Result downloadHtmlImage(Long newsId, String fileUrl, String fileType, String fileName) throws Exception {
        String filePath = FileUtil.downloadHtmlFile(newsId, fileUrl, fileType, fileName);
        String url = "http://localhost:9000" + filePath;
        return ResultUtils.success(url);
    }

    @ApiOperation(value = "按类型下载", notes = "通用上传详情")
    @GetMapping("/test")
    public Result test() throws Exception {
         File file = new File("D:\\workspace\\local-workspace\\news\\uploadPath\\html\\1\\download.html");
         BufferedReader reader = new BufferedReader(new FileReader(file));
         StringBuilder stringBuilder = new StringBuilder();

         String line;
         while ((line = reader.readLine()) != null) {
             stringBuilder.append(line).append("\n");
         }

         reader.close();
//        String html = HttpUtil.get("https://mp.weixin.qq.com/s?__biz=MzIxNDUyMzcwOA==&mid=2247784761&idx=1&sn=ec73bc3a37e75b10c6b9f933a7c6f379&scene=19");
        // String html = readFileByUrl("http://mp.weixin.qq.com/s?__biz=MzIxNDUyMzcwOA==&mid=2247784761&idx=1&sn=ec73bc3a37e75b10c6b9f933a7c6f379&scene=19#wechat_redirect");

        List<Map<String, Object>> fileList = new ArrayList<>();
         String html = stringBuilder.toString();
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
                if (href.endsWith(".ico")) {
                    String fileType = "ico";
                    String fileName = fileType + "_" + icoNumber + ".ico";
                    icoNumber ++;
                    Map<String, Object> map = new HashMap<>();
                    map.put("fileType", fileType);
                    map.put("fileName", fileName);
                    map.put("fileUrl", "https:" +href);
                    fileList.add(map);
                    linkElement.attr("href", "./" + fileType + "/" + fileName);
                } else if (href.endsWith(".png")) {
                    String fileType = "image";
                    String fileName =  fileType + "_" + imageNumber + ".png";
                    imageNumber ++;
                    Map<String, Object> map = new HashMap<>();
                    map.put("fileType", fileType);
                    map.put("fileName", fileName);
                    map.put("fileUrl", "https:" +href);
                    fileList.add(map);
                    linkElement.attr("href", "./" + fileType + "/" + fileName);
                } else if (href.endsWith(".jpg")) {
                    String fileType = "image";
                    String fileName =  fileType + "_" + imageNumber + ".jpg";
                    imageNumber ++;
                    Map<String, Object> map = new HashMap<>();
                    map.put("fileType", fileType);
                    map.put("fileName", fileName);
                    map.put("fileUrl", "https:" +href);
                    fileList.add(map);
                    linkElement.attr("href", "./" + fileType + "/" + fileName);
                } else if (href.endsWith(".jpeg")) {
                    String fileType = "image";
                    String fileName =  fileType + "_" + imageNumber + ".jpeg";
                    imageNumber ++;
                    Map<String, Object> map = new HashMap<>();
                    map.put("fileType", fileType);
                    map.put("fileName", fileName);
                    map.put("fileUrl", "https:" +href);
                    fileList.add(map);
                    linkElement.attr("href", "./" + fileType + "/" + fileName);
                } else if (href.endsWith(".css")) {
                    String fileType = "css";
                    String fileName =  fileType + "_" + cssNumber + ".css";
                    cssNumber ++;
                    Map<String, Object> map = new HashMap<>();
                    map.put("fileType", fileType);
                    map.put("fileName", fileName);
                    map.put("fileUrl", "https:" +href);
                    fileList.add(map);
                    linkElement.attr("href", "./" + fileType + "/" + fileName);
                } else if (href.endsWith(".js")) {
                    // 删除
                    linkElement.remove();
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
                Map<String, Object> map = new HashMap<>();
                map.put("fileType", fileType);
                map.put("fileName", fileName);
                map.put("fileUrl", dataSrc);
                fileList.add(map);
                imgElement.attr("src", "./" + fileType + "/" + fileName);
                imgElement.removeAttr("data-src");
            }
        }

        // 判断是否存在iframe视频
        Elements videoImage = document.getElementsByClass("video_iframe");

        System.out.println(imgElements);
        for (Map<String, Object> singleFile : fileList) {
            String fileUrl = singleFile.get("fileUrl").toString();
            String fileName = singleFile.get("fileName").toString();
            String fileType = singleFile.get("fileType").toString();
            String s = FileUtil.downloadHtmlFile(1L, fileUrl, fileType, fileName);
        }
        String html1 = document.html();
        System.out.println(html1);

        try {
            File outfile = new File("C:\\workspace\\java-workspace\\news\\news_server\\uploadPath\\html\\1\\download1.html");
            FileWriter writer = new FileWriter(outfile);
            writer.write(html1);
            writer.close();
            System.out.println("String has been written to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtils.success("test");
    }

    public static String readFileByUrl(String urlStr) {
        String res=null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3*1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            InputStream inputStream = conn.getInputStream();
            res = readInputStream(inputStream);
        } catch (Exception e) {
            System.out.println("通过url地址获取文本内容失败 Exception：" + e);
        }
        return res;
    }

    public static String readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        System.out.println(new String(bos.toByteArray(),"utf-8"));
        return new String(bos.toByteArray(),"utf-8");
    }

    public static void main(String[] args) throws IOException {
        if (1 != 1) {
            String s = HttpUtil.get("https://mp.weixin.qq.com/s?__biz=MzIxNDUyMzcwOA==&mid=2247829324&idx=1&sn=590c04504c3be2b61007aaaa4cf53441");
            System.out.println(s);
            return ;
        }

        File file = new File("D:\\workspace\\local-workspace\\news\\uploadPath\\html");
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.isFile()) {
                continue;
            }
            File[] files1 = file1.listFiles(new FilenameFilter() {
                //以config_db开头，文件后缀为.properties的将被选出来，其余被过滤掉
                @Override
                public boolean accept(File dir, String name) {
                    String fileName = name.toLowerCase();
                    if (fileName.endsWith("46986.html")
                    || fileName.endsWith("46987.html")
                            || fileName.endsWith("46988.html")
                            || fileName.endsWith("46988.html")) {
                        return false;
                    }
                    if (fileName.endsWith(".html")) {
                        return true;
                    }
                    return false;
                }
            });
            if (files1.length > 0) {
                File file2 = files1[0];
                String filePath = file2.getAbsolutePath();
                System.out.println(file2.getAbsolutePath());

                BufferedReader reader = new BufferedReader(new FileReader(file2));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                reader.close();
                String html = stringBuilder.toString();

                if (html.contains("未知错误")) {
                    throw new RuntimeException("filePath");
                }

                Document document = Jsoup.parse(html);

                Element qrCodeImgElement = document.getElementById("js_pc_qr_code_img");
                if (qrCodeImgElement == null) {
                    continue;
                }
                qrCodeImgElement.attr("src", "../qrcode.bmp");
                qrCodeImgElement.removeAttr("id");
                System.out.println(document.html());
            try {
                File outfile = new File(filePath);
                FileWriter writer = new FileWriter(outfile);
                writer.write(document.html());
                writer.close();
                System.out.println("String has been written to the file.");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            }
        }
    }

    @ApiOperation(value = "按类型下载", notes = "通用上传详情")
    @GetMapping("/getNewsFromOtherCompute")
    public Result getNewsFromOtherCompute() throws Exception {
        String url = loadnewsUrl;
        String wechatinfoFile = loadnewsFileName;
        String filePath = FileUploadUtils.defaultBaseDir + File.separator + wechatinfoFile;
        FileUtil.downloadFile(url + wechatinfoFile, filePath);
        String newsWechatJson = FileUtil.readFile(filePath);
        List<NewsWechatDTO> latestNewsWechatDTO = new ArrayList<>();
        Set<String> aidSet = new HashSet<>();

        // 判断是否有值
        if (StringUtils.isNotEmpty(newsWechatJson)) {
            // 插入时，需要去重
            List<NewsWechatDTO> oldNewsWechatDTO = JSONArray.parseArray(newsWechatJson, NewsWechatDTO.class);
            if (CollectionUtil.isNotEmpty(oldNewsWechatDTO)) {
                latestNewsWechatDTO.addAll(oldNewsWechatDTO);
                // 获取aid唯一表示集合
                aidSet.addAll(oldNewsWechatDTO.stream().map(NewsWechatDTO::getAid).collect(Collectors.toSet()));
            }
        }
        // 构建拉取结果对象
        for (int i = 0; i < latestNewsWechatDTO.size(); i++) {
            NewsWechatDTO newsWechatDTO = latestNewsWechatDTO.get(i);
            // 如果还没有打包好，则不进行上传
            if (Constants.DealFlag.UN_DEAL == newsWechatDTO.getDealZip()) {
                continue;
            }

            // 往后的步骤都代表已经上传了
            newsWechatDTO.setDealUpload(Constants.DealFlag.DEALED);
            // 判断是否已经保存
            if (newsWechatService.countByAid(newsWechatDTO.getAid()) > 0) {
                // 需要删除掉了
                continue;
            }

            // 先插入数据库，在处理静态资源
            NewsWechat newsWechat = BeanUtil.copyProperties(newsWechatDTO, NewsWechat.class);
            newsWechatService.save(newsWechat);

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
            newsService.save(news);

            // 成功了之后，插入关联
            NewsWechatRel newsWechatRel = new NewsWechatRel();
            newsWechatRel.setNewsWechatId(newsWechat.getId());
            newsWechatRel.setNewsId(news.getNewsId());
            newsWechatRel.setCreateTime(new Date());
            newsWechatRelService.save(newsWechatRel);

            try {
                // 拉取对应的zip包了
                String zipName = newsWechatDTO.getAid() + ".zip";
                String zipUrl = url + zipName;
                String defaultHtmlDownloadDir = FileUtil.getDefaultHtmlDownloadDir();
                String downloadZipFilePath = FileUtil.getDefaultHtmlDownloadDir() + File.separator + zipName;
                FileUtil.downloadFile(zipUrl, downloadZipFilePath);
                // 解压文件
                ZipUtil.unzip(downloadZipFilePath, defaultHtmlDownloadDir);
                // 删除压缩包
                FileUtil.deleteFile(downloadZipFilePath);

                // 新闻图片地址修改
                news.setNewsImage("/resource/html/" + newsWechatDTO.getAid() + "/cover/" + newsWechatDTO.getCoverName());
                // 静态资源地址修改
                news.setStaticFilePath("/resource/html/" + newsWechatDTO.getAid() + "/html/" + newsWechatDTO.getStaticName());
                news.setToStatic(1);
                newsService.saveOrUpdate(news);
            } catch (Exception e) {
                System.out.println("静态资源拉取失败");
            }
        }
        // 反馈文件状态
        try {
            String updateFileUrl = url + loadnewsUpdateFileApi;
            Map<String, Object> params = new HashMap<>();
            params.put("newsWechats", latestNewsWechatDTO);
            gatewayClient.doJSONPost(updateFileUrl, params, new HashMap<>());
        } catch (Exception e) {
            System.out.println("反馈失败");
        }
        return ResultUtils.success();
    }

}
