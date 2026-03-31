package com.shisfish.news.common.utils.fastdfs;


import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ServerInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 实现FastDFS文件管理
 * 文件上传
 * 文件删除
 * 文件下载
 * 文件信息获取
 * Storage信息获取
 * Tracker信息获取
 * @Version: 1.0.0
 */
public class FastDFSClient {

    static {
        try {
            // 查找classpath下的文件路径
            String fileName = new ClassPathResource("fdfs_client.conf").getPath();
            // 加载Tracker连接信息
            ClientGlobal.init(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件上传
     *
     * @param file FastDFSFile
     */
    public static String[] upload(FastDFSFile file) {
        // 获取文件作者
        NameValuePair[] metaList = new NameValuePair[1];
        metaList[0] = new NameValuePair(file.getAuthor());
        /*
         * 文件上传后的返回值
         * uploadResults[0]:文件上传所存储的组名，例如:group1
         * uploadResults[1]:文件存储路径,例如：M00/00/00/wKjThF0DBzaAP23MAAXz2mMp9oM26.jpeg
         */
        String[] uploadResults = null;
        try {
            // 获取StorageClient对象
            StorageClient storageClient = getStorageClient();
            /*
             *  file_buff 上传文件的字节数组
             *  file_ext_name 文件的拓展名
             *  meta_list 附加参数 如: 拍摄地址:北京
             */
            // 通过StorageClient访问Storage,实现文件上传,并且获取文件上传后的存储信息
            assert storageClient != null;
            uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), metaList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uploadResults;
    }

    /**
     * 获取文件信息
     *
     * @param groupName:             组名 group1
     * @param remoteFileName：文件存储路径名 M00/00/00/wKjThF0DBzaAP23MAAXz2mMp9oM26.jpeg
     */
    public static FileInfo getFileInfo(String groupName, String remoteFileName) {
        try {
            // 获取StorageClient对象
            StorageClient storageClient = getStorageClient();
            // 通过StorageClient访问Storage,返回文件上传后的存储信息
            assert storageClient != null;
            return storageClient.get_file_info(groupName, remoteFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 文件下载
     *
     * @param groupName:             组名 group1
     * @param remoteFileName：文件存储路径名 M00/00/00/wKjThF0DBzaAP23MAAXz2mMp9oM26.jpeg
     */
    public static InputStream downFile(String groupName, String remoteFileName) {
        try {
            // 获取StorageClient对象
            StorageClient storageClient = getStorageClient();
            // 通过StorageClient访问Storage,下载文件
            assert storageClient != null;
            byte[] fileBytes = storageClient.download_file(groupName, remoteFileName);
            return new ByteArrayInputStream(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param groupName:             组名 group1
     * @param remoteFileName：文件存储路径名 M00/00/00/wKjThF0DBzaAP23MAAXz2mMp9oM26.jpeg
     */
    public static void deleteFile(String groupName, String remoteFileName) {
        try {
            // 获取StorageClient对象
            StorageClient storageClient = getStorageClient();
            // 通过StorageClient访问Storage,删除文件
            assert storageClient != null;
            storageClient.delete_file(groupName, remoteFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Storage信息
     *
     * @param groupName: 组名 group1
     */
    public static StorageServer getStorages(String groupName) {
        try {
            // 创建TrackerClient客户端对象
            TrackerClient trackerClient = new TrackerClient();
            // 通过TrackServer获取Storage连接信息
            TrackerServer trackerServer = trackerClient.getConnection();
            // 获取Storage信息
            return trackerClient.getStoreStorage(trackerServer, groupName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 获取Storage服务的IP、端口信息
     * @param groupName :             组名 group1
     * @param remoteFileName ：文件存储路径名 M00/00/00/wKjThF0DBzaAP23MAAXz2mMp9oM26.jpeg
     */
    public static ServerInfo[] getServerInfo(String groupName, String remoteFileName) {
        try {
            // 创建TrackerClient客户端对象
            TrackerClient trackerClient = new TrackerClient();
            // 通过TrackerClient访问TrackServer服务,获取连接信息
            TrackerServer trackerServer = trackerClient.getConnection();
            return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取Tracker服务地址
     *
     * @return String
     */
    public static String getTrackerUrl() {
        try {
            // 创建TrackerClient客户端对象
            TrackerClient trackerClient = new TrackerClient();
            // 通过TrackerClient访问TrackServer服务,获取连接信息
            TrackerServer trackerServer = trackerClient.getConnection();
            // 获取Tracker地址
            return "http://" + trackerServer.getInetSocketAddress().getHostString() + ":" + ClientGlobal.getG_tracker_http_port();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取Tracker信息
     */
    public static TrackerServer getTrackerServer() {
        try {
            // 创建TrackerClient客户端对象
            TrackerClient trackerClient = new TrackerClient();
            // 通过TrackerClient访问TrackServer服务,返回连接信息
            return trackerClient.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取StorageClient
     */
    public static StorageClient getStorageClient() {
        try {
            // 获取TrackerServer
            TrackerServer trackerServer = getTrackerServer();
            // 返回StorageClient
            return new StorageClient(trackerServer, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws IOException {
        // 1.获取文件信息
        FileInfo info = getFileInfo("group1", "M00/00/00/wKiKlF8lN8KAL90bAAAS-kwhS7s557.png");
        System.out.println(info);

        // 2.文件下载
        InputStream is = downFile("group1", "M00/00/00/wKiKlF8lN8KAL90bAAAS-kwhS7s557.png");
        // 将文件写入本地磁盘
        FileOutputStream os = new FileOutputStream("/Users/jay/Desktop/idea_codes/黑马复习/images/2.jpg");
        // 定义缓冲区
        byte[] buffer = new byte[1024];
        while (is.read(buffer) != -1) {
            os.write(buffer);
        }
        os.flush();
        os.close();
        is.close();

        // 3.删除文件
        deleteFile("group1", "M00/00/00/wKiKlF8lN8KAL90bAAAS-kwhS7s557.png");

        // 4.获取Storage信息
        StorageServer storageServer = getStorages("group1");
        System.out.println(storageServer.getStorePathIndex());

        // 5.获取Storage的IP,端口信息
        ServerInfo[] serverInfos = getServerInfo("group1", "M00/00/00/wKiKlF8lN8KAL90bAAAS-kwhS7s557.png");
        Arrays.stream(serverInfos).forEach(serverInfo -> {
            System.out.println("ip:" + serverInfo.getIpAddr() + ":" + serverInfo.getPort());
        });

        // 6.获取Tracker信息
        TrackerServer trackerServer = getTrackerServer();
        System.out.println(trackerServer.getInetSocketAddress());

        // 7.获取StorageClient
        StorageClient storageClient = getStorageClient();
        System.out.println(storageClient);

    }
}
