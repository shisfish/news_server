package com.shisfish.news.web.controller.spider;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shisfish.news.common.config.NewsConfig;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.common.utils.file.FileUtil;
import com.shisfish.news.dao.datatransferobject.biz.SpiderNewsDTO;
import com.shisfish.news.dao.domain.biz.News;
import com.shisfish.news.service.service.biz.INewsService;
import com.shisfish.news.service.service.biz.INewsWechatService;
import com.shisfish.news.service.service.biz.ISlaveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author: shisfish
 * @Date: 2023/10/26
 * @Description:
 * @Version: 1.0.0
 */
@Api(tags = "爬虫接口")
@RestController
@RequestMapping("/news/spider")
public class SpiderController {

    @Autowired
    private INewsWechatService newsWechatService;

    @Autowired
    private INewsService newsService;

    @Autowired
    private ISlaveService slaveService;

    @Autowired
    NewsConfig newsConfig;

    @ApiOperation(value = "爬虫获取最新文章", notes = "爬虫获取最新文章")
    @GetMapping(value = "/saveBySpider")
    public Result saveBySpider() {
        newsWechatService.saveLatestNewsWechatBySpider();
        return ResultUtils.success();
    }

    @ApiOperation(value = "爬虫获取最新文章", notes = "爬虫获取最新文章")
    @GetMapping(value = "/mainNews")
    public Result mainNews(Integer begin) {
        String token = "1575716617";
        String cookie = "pgv_pvid=1678652964456965; ua_id=gyzc7BBdphV7eH1BAAAAAG7V_xqtzlpQbG1phmcV2Hw=; wxuin=93968336397973; mm_lang=zh_CN; RK=qzvsjSydEY; ptcz=f022b57cdc8c34666c3d9d74a939226531a7278cad817802a7a33d03f8d866ed; pac_uid=0_8466b520957b8; iip=0; qq_domain_video_guid_verify=d4ca0f7a4bf97c95; _clck=ljb54x|1|fh6|0; uuid=5685de24c2a74f2870fec94c59403073; rand_info=CAESIJ9deoyMBUquyG4g4g8tWhlxIwwS9HVGlzmSBRul+Qzl; slave_bizuin=3937571426; data_bizuin=3937571426; bizuin=3937571426; data_ticket=O2PGn/gx4+iW4iSJT/mB64pRZqmWwv0cqGZxrRQ1QwMOo0toayfoJ0f5iKHZY+0V; slave_sid=T2dQalYyME5aYTJGdm9NV1dKSUllQ1owNWM3TkdZRkJBYmRTNkJ2X0ZTVTFJN2N1Y0NWUDJfRjRCTHRKUGRPS01zQVFLSThFakowUThSM2FjNUJiVHFTUkZNV1NyS2YyU1VYNE5nSmlhUTlkdzVrR21XSWpCWTRPVXNXTmJPQW1Mak9XRXZib24wZXplZlFi; slave_user=gh_30e603142f07; xid=191a9a5a9413643551572f6527f0aa59; _clsk=msv27o|1701421240861|2|1|mp.weixin.qq.com/weheat-agent/payload/record";
        newsWechatService.saveLatestNewsWechatFromApp(begin, token, cookie);
        return ResultUtils.success();
    }

    @ApiOperation(value = "获取新闻详细信息", notes = "获取新闻详细信息详情")
    @GetMapping(value = "/saveFromNewsWechat")
    public Result saveFromNewsWechat() {
        newsService.saveNewsFromWechat();
        return ResultUtils.success();
    }

    @ApiOperation(value = "新闻静态化", notes = "新闻静态化")
    @GetMapping(value = "/loadNewsToStatic")
    public Result loadNewsToStatic() {
        newsService.loadNewsToStatic();
        return ResultUtils.success();
    }

    @ApiOperation(value = "爬虫获取最新文章", notes = "爬虫获取最新文章")
//    @PreAuthorize("@ss.hasPermi('news:news:edit')")
    @PostMapping(value = "/spiderNews")
    public Result spiderNews(@RequestBody SpiderNewsDTO spiderNewsDTO) {
        List<Long> newsIds = newsWechatService.spiderNews(spiderNewsDTO);
        String message;
        if (CollectionUtil.isEmpty(newsIds)) {
            message = "本次未拉取到文章";
        } else {
            message = "本次共拉取到" + newsIds.size() + "篇文章";
        }
        return ResultUtils.success(message);
    }

    @ApiOperation(value = "爬虫-提供给旧服务器使用", notes = "爬虫-提供给旧服务器使用")
    @PostMapping(value = "/spiderNewsForOld")
    public Result spiderNewsForOld(@RequestBody SpiderNewsDTO spiderNewsDTO) {
        List<Long> newsIds = newsWechatService.spiderNews(spiderNewsDTO);
        if (CollectionUtil.isEmpty(newsIds)) {
            return ResultUtils.success(CollectionUtils.EMPTY_COLLECTION);
        }
        // 需要把所有新闻属性全部传过去
        return ResultUtils.success(newsService.findNewsByIds(newsIds));
    }

    @ApiOperation(value = "按类型下载", notes = "通用上传详情")
    @PostMapping("/getNewsFromOtherCompute")
    public Result getNewsFromOtherCompute(@RequestBody SpiderNewsDTO spiderNewsDTO) throws Exception {
        String url = newsConfig.getNewServerUrl() + "/prod-api/news/spider/spiderNewsForOld";
        String response = HttpUtil.createPost(url)
                .body(JSONObject.toJSONString(spiderNewsDTO))
                .header("Content-Type", "application/json")
                .execute()
                .body();
        JSONObject jsonObject = JSONObject.parseObject(response);
        Integer code = jsonObject.getInteger("code");
        if (code != 200) {
            return ResultUtils.error(jsonObject.getString("msg"));
        }
        JSONArray data = jsonObject.getJSONArray("data");
        if (CollectionUtil.isEmpty(data)) {
            return ResultUtils.success("本次未拉取到文章");
        }
        List<News> newsList = data.toJavaList(News.class);
        newsList.stream().forEach(news -> {
            news.setNewsId(null);
            // 图片附件需要获取
            // 获取真实图片地址
            String imageUrl = newsConfig.getNewServerUrl() + news.getNewsImage();
            String newNewsImage = null;
            try {
                newNewsImage = FileUtil.downloadImage(imageUrl);
            } catch (Exception e) {
                System.out.println("下载微信图片失败：" + e.getMessage());
            }
            news.setNewsImage(newNewsImage);
        });
        newsService.saveBatch(newsList);
        return ResultUtils.success("本次共拉取到" + newsList.size() + "篇文章");
    }

    private static final String DB_URL = "jdbc:mysql://10.0.50.31:3306/dev_orderall";
    private static final String USER = "huangshun";
    private static final String PASS = "8GbjEkWdJm!e";
//    public static void main(String[] args) {
//        String tableName = "sys_oper_log"; // Replace with your table name
//        generateInsertStatements(tableName);
//    }

    public static void generateInsertStatements(String tableName) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        FileWriter fileWriter = null;

        try {
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Open a connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Execute a query to get all data from the table
            stmt = conn.createStatement();
            String sql = "SELECT * FROM " + tableName;
            rs = stmt.executeQuery(sql);

            // Get metadata to retrieve column names
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Create or open the SQL file for writing
            fileWriter = new FileWriter(tableName + ".sql");
            StringBuilder header = new StringBuilder("start /dmdata/build/backend/" + tableName + ".sql\ndelete from NEWS." + tableName.toUpperCase() + "; \nselect * from NEWS." + tableName.toUpperCase() + ";\nSET IDENTITY_INSERT NEWS." + tableName.toUpperCase() + " ON;\n");
            fileWriter.write(header.toString());
            // Process the result set and write insert statements
            while (rs.next()) {
                StringBuilder insertStatement = new StringBuilder("INSERT INTO NEWS." + tableName.toUpperCase() + " (");
                for (int i = 1; i <= columnCount; i++) {
                    insertStatement.append(metaData.getColumnName(i).toUpperCase());
                    if (i < columnCount) {
                        insertStatement.append(", ");
                    }
                }
                insertStatement.append(") VALUES (");
                for (int i = 1; i <= columnCount; i++) {
                    insertStatement.append("'").append(rs.getString(i)).append("'");
                    if (i < columnCount) {
                        insertStatement.append(", ");
                    }
                }
                insertStatement.append(");\n");
                fileWriter.write(insertStatement.toString());
            }
            StringBuilder bottom = new StringBuilder("\nSET IDENTITY_INSERT NEWS." + tableName.toUpperCase() + " OFF;\n/");
            fileWriter.write(bottom.toString());
            System.out.println("Insert statements have been written to " + tableName + ".sql");
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (SQLException | IOException se) {
                se.printStackTrace();
            }
        }
    }

    // 遍历文件夹下所有文件
    private static void loopRead(File dir, StringBuffer sb) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    loopRead(file, sb);
                } else if (file.getName().endsWith("java")) {
                    if (file.length() != 0) {
                        sb.append(readFileToString(file));
                    }
                }
            }
        }

    }

    //读取文件里面的内容
    private static String readFileToString(File file) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                String s = line.trim();
                if (s.length() == 0) {
                    continue;
                }
                if (s.startsWith("/") || s.startsWith("*")) {
                    continue;
                }
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return sb.toString();

    }

    //将读取的路径以及相应的内容写入指定的文件
    private static void write(String str, Writer writer) {
        try {
            writer.write(str);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

//    public static void main(String[] args) {
//        String createTableSql = "CREATE TABLE `banner` (" +
//                "`id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id'," +
//                "`name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述'," +
//                "`image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '图片地址'," +
//                "`status` int DEFAULT NULL COMMENT '审核状态（0.审核中 1.审核成功 2.审核失败）'," +
//                "`order_num` int NOT NULL DEFAULT '0' COMMENT '链接显示顺序'," +
//                "`create_by` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者'," +
//                "`create_time` datetime DEFAULT NULL COMMENT '创建时间'," +
//                "`update_by` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者'," +
//                "`update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间'," +
//                "`remark` varchar(511) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注'," +
//                "PRIMARY KEY (`id`) USING BTREE" +
//                ") ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='Banner表';";
//        try {
//            createWordTableFromSql(createTableSql, "output.docx");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
        public static void main(String[] args) throws Exception {
        //文件读取路径
//        File dir = new File("D:\\workspace\\coding-workspace\\news_server");
//        //文件输出路径
//        File target = new File("D:\\test.txt");
//        BufferedWriter bw = new BufferedWriter(new FileWriter(target));
//
//        StringBuffer sb = new StringBuffer();
//        loopRead(dir, sb);
//        write(sb.toString(), bw);

            String filePath = "D:\\workspace\\coding-workspace\\news_server\\web\\src\\main\\java\\com\\shisfish\\news\\web\\controller\\spider\\text.json"; // 替换为你的文件路径
            StringBuilder contentBuilder = new StringBuilder();

            try (Scanner scanner = new Scanner(new File(filePath))) {
                while (scanner.hasNextLine()) {
                    contentBuilder.append(scanner.nextLine()).append("\n");
                }
            } catch (FileNotFoundException e) {
                System.err.println("An error occurred while reading the file: " + e.getMessage());
            }

            JSONArray arrays = JSONArray.parseArray(contentBuilder.toString());
            Map<String, Long> publishTime = arrays.stream()
                    .collect(Collectors.groupingBy(data ->
                                    ((JSONObject) data).getString("publishTime").substring(0, 10),
                            Collectors.counting()
                    ));
            publishTime.keySet().forEach(a -> System.out.println(a + " " + publishTime.get(a)));

        }

    public static void createWordTableFromSql(String sql, String outputPath) throws Exception {
        // 解析SQL语句，提取字段信息
        // 使用正则表达式提取字段信息
        Pattern pattern = Pattern.compile("`(\\w+)`\\s+([^\\s]+)COMMENT\\s+'([^']*)'");
        Matcher matcher = pattern.matcher(sql);

        // 创建一个新的Word文档
        XWPFDocument document = new XWPFDocument();

        // 创建一个表格
        XWPFTable table = document.createTable();

        // 添加表头
        XWPFTableRow headerRow = table.getRow(0);
        if (headerRow == null) {
            headerRow = table.createRow();
        }
        headerRow.getCell(0).setText("字段名称");
        headerRow.addNewTableCell().setText("字段类型");
        headerRow.addNewTableCell().setText("字段描述");

        // 填充表格数据
        int rowNum = 1;
        while (matcher.find()) {
            XWPFTableRow row = table.createRow();
            // 字段名称
            row.getCell(0).setText(matcher.group(1));
            // 字段类型
            row.getCell(1).setText(matcher.group(2));
            // 字段描述
            row.getCell(2).setText(matcher.group(4));
            rowNum++;
        }

        // 保存Word文档
        try (FileOutputStream out = new FileOutputStream("table.docx")) {
            document.write(out);
        }

        System.out.println("表格已成功生成！");
    }

    @ApiOperation(value = "爬虫-提供给旧服务器使用", notes = "爬虫-提供给旧服务器使用")
    @GetMapping(value = "/copyToBack")
    public Result copyToBack() {
        newsService.copyToBack();
        return ResultUtils.success();
    }

    @ApiOperation(value = "爬虫-提供给旧服务器使用", notes = "爬虫-提供给旧服务器使用")
    @GetMapping(value = "/copyToBm")
    public Result copyToBm() {
        newsService.copyToBm();
        return ResultUtils.success();
    }

    @ApiOperation(value = "爬虫-提供给旧服务器使用", notes = "爬虫-提供给旧服务器使用")
    @GetMapping(value = "/testSlave")
    public Result testSlave(@RequestParam("tableName") String tableName) {
        return ResultUtils.success(slaveService.testSlave(tableName));
    }

}
