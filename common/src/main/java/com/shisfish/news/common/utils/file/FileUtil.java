package com.shisfish.news.common.utils.file;

import com.shisfish.news.common.config.NewsConfig;
import com.shisfish.news.common.constant.Constants;
import com.shisfish.news.common.utils.DateUtils;
import com.shisfish.news.common.utils.string.StringUtils;
import com.shisfish.news.common.utils.uuid.IdUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 文件处理工具类
 */
public class FileUtil {
    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    /**
     * 默认上传的地址
     */
    private static String defaultDownloadDir = NewsConfig.getDownloadPath();

    public static void setDefaultDownloadDir(String defaultDownloadDir) {
        FileUtil.defaultDownloadDir = defaultDownloadDir;
    }

    public static String getDefaultDownloadDir() {
        return defaultDownloadDir;
    }

    private static String defaultHtmlDownloadDir = NewsConfig.getHtmlDownloadPath();

    public static void setDefaultHtmlDownloadDir(String defaultHtmlDownloadDir) {
        FileUtil.defaultHtmlDownloadDir = defaultHtmlDownloadDir;
    }

    public static String getDefaultHtmlDownloadDir() {
        return defaultHtmlDownloadDir;
    }

    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os       输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException {
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath) {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 文件名称验证
     *
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename) {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 下载文件名重新编码
     *
     * @param request  请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName)
            throws UnsupportedEncodingException {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE")) {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        } else if (agent.contains("Chrome")) {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }

    public static String downloadImage(String imageUrl) throws Exception {

        //定义一个URL对象，就是你想下载的图片的URL地址
        URL url = new URL(imageUrl);
        //打开连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为10秒
        conn.setConnectTimeout(10 * 1000);
        //通过输入流获取图片数据
        InputStream is = conn.getInputStream();
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性
        byte[] data = readInputStream(is);
        //创建一个文件对象用来保存图片，默认保存当前工程根目录，起名叫Copy.jpg

        String downloadDir = getDefaultDownloadDir();
        String fileName = extractFilename();

        File file = getAbsoluteFile(downloadDir, fileName);
        //创建输出流
        FileOutputStream outStream = new FileOutputStream(file);
        //写入数据
        outStream.write(data);
        //关闭输出流，释放资源
        outStream.close();

        return getPathFileName(downloadDir, fileName);
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[6024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    public static final String extractFilename() {
        String fileName;
        String extension = "jpg";
        fileName = DateUtils.datePath() + "/" + IdUtils.fastUUID() + "." + extension;
        return fileName;
    }

    private static final String getPathFileName(String uploadDir, String fileName) throws IOException {
        int dirLastIndex = NewsConfig.getLocalFilePath().length() + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        String pathFileName = Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
        return pathFileName;
    }

    public static final File getAbsoluteFile(String dir, String fileName) throws IOException {
        File desc = new File(dir + File.separator + fileName);

        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists()) {
            desc.createNewFile();
        }
        return desc;
    }



    public static String downloadHtmlFile(Long newsId, String fileUrl, String fileType, String fileName) throws Exception {

        //定义一个URL对象，就是你想下载的图片的URL地址
        URL url = new URL(fileUrl);
        //打开连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为10秒
        conn.setConnectTimeout(10 * 1000);
        //通过输入流获取图片数据
        InputStream is = conn.getInputStream();
        // 得到图片的二进制数据，以二进制封装得到数据，具有通用性
        byte[] data = readInputStream(is);
        //创建一个文件对象用来保存图片，默认保存当前工程根目录，起名叫Copy.jpg

        String htmlDownloadDir = getDefaultHtmlDownloadDir();
        String relativeFileName = newsId + "/" + fileType + "/" + fileName;

        File file = getAbsoluteFile(htmlDownloadDir, relativeFileName);
        //创建输出流
        FileOutputStream outStream = new FileOutputStream(file);
        //写入数据
        outStream.write(data);
        //关闭输出流，释放资源
        outStream.close();

        return getPathFileName(htmlDownloadDir, relativeFileName);
    }

    public static void downloadFile(String fileUrl, String filePath) throws Exception {
        //定义一个URL对象，就是你想下载的图片的URL地址
        URL url = new URL(fileUrl);
        //打开连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为10秒
        conn.setConnectTimeout(10 * 1000);
        //通过输入流获取图片数据
        InputStream is = conn.getInputStream();
        // 得到图片的二进制数据，以二进制封装得到数据，具有通用性
        byte[] data = readInputStream(is);
        //创建一个文件对象用来保存图片，默认保存当前工程根目录，起名叫Copy.jpg

        File file = new File(filePath);
        createDir(file);
        //创建输出流
        FileOutputStream outStream = new FileOutputStream(file);
        //写入数据
        outStream.write(data);
        //关闭输出流，释放资源
        outStream.close();
    }

    public static final void createDir(String filePath) throws IOException {
        File file = new File(filePath);
        createDir(file);
    }

    public static final void createDir(File file) throws IOException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public static final String readFile(String filePath) throws IOException {
        return readFile(new File(filePath));
    }

    public static final String readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }

        reader.close();
        return stringBuilder.toString();
    }

    public static void deleteFiles(String dir) {
        deleteFiles(new File(dir));
    }

    public static void deleteFiles(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFiles(file);
                }
            }
        }
        dir.delete();
    }

    public static final void writeFile(String filePath, String content) throws IOException {
        writeFile(new File(filePath), content);
    }

    public static final void writeFile(File file, String content) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
    }

}
