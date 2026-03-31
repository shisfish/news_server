package com.shisfish.news.web.controller.common;

import com.shisfish.news.common.config.NewsConfig;
import com.shisfish.news.common.constant.Constants;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.common.utils.fastdfs.FastDFSClient;
import com.shisfish.news.common.utils.fastdfs.FastDFSFile;
import com.shisfish.news.common.utils.file.FileUploadUtils;
import com.shisfish.news.common.utils.file.FileUtil;
import com.shisfish.news.common.utils.string.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @Author: shisfish
 * @Date: 22023/8/16
 * @Description: 文件上传接口
 * @Version: 1.0.0
 */
@Api(tags = "文件上传接口")
@Slf4j
@RestController
public class CommonController {

    @Autowired
    private NewsConfig newsConfig;

    @ApiOperation(value = "FastDFS单文件上传", notes = "FastDFS单文件上传上传详情")
    @PostMapping("/common/fast/upload")
    public Result<HashMap<String, Object>> upload(MultipartFile file) throws IOException {
        // 封装文件信息
        FastDFSFile fastDFSFile = new FastDFSFile(
                // 文件名 1.jpg
                file.getOriginalFilename(),
                // 文件字节数组
                file.getBytes(),
                // 文件拓展名 jpg
                org.springframework.util.StringUtils.getFilenameExtension(file.getOriginalFilename())
        );
        // 文件上传
        String[] uploadResults = FastDFSClient.upload(fastDFSFile);
        // 拼接文件上传地址
        String url = FastDFSClient.getTrackerUrl() + "/" + uploadResults[0] + "/" + uploadResults[1];
        return ResultUtils.success(new HashMap<String, Object>() {{
            put("url", url);
        }});
    }


    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @ApiOperation(value = "通用下载", notes = "通用下载详情")
    @GetMapping("/common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtil.isValidFilename(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = NewsConfig.getDownloadPath() + fileName;

            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + FileUtil.setFileDownloadHeader(request, realFileName)
            );
            FileUtil.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtil.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求
     */
    @ApiOperation(value = "通用上传", notes = "通用上传详情")
    @PostMapping("/common/upload")
    public Result<HashMap<String, Object>> uploadFile(MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String filePath = NewsConfig.getUploadPath();
            log.debug("上传路径: {}", filePath);
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = newsConfig.getLocalFileUrl() + fileName;
            return ResultUtils.success(new HashMap<String, Object>() {{
                put("fileName", fileName);
                put("url", url);
            }});
        } catch (Exception e) {
            return ResultUtils.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @ApiOperation(value = "本地资源通用下载", notes = "本地资源通用下载详情")
    @GetMapping("/common/download/resource")
    public void resourceDownload(String name, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 本地资源路径
        String localPath = NewsConfig.getLocalFilePath();
        // 数据库资源地址
        String downloadPath = localPath + StringUtils.substringAfter(name, Constants.RESOURCE_PREFIX);
        // 下载名称
        String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + FileUtil.setFileDownloadHeader(request, downloadName)
        );
        FileUtil.writeBytes(downloadPath, response.getOutputStream());
    }
}
