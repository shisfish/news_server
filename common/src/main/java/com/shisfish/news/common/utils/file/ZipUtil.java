package com.shisfish.news.common.utils.file;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shuan
 * @date 2023/11/4
 */
public class ZipUtil {

    public static void zipDir(String filePath, String zipName) throws ZipException {
        zipDir(new File(filePath), zipName);
    }

    public static void zipDir(File dir, String zipName) throws ZipException {
        ZipFile zipFile = new ZipFile(zipName);
        zipFile.addFolder(dir);
    }

    public static void unzip(String zipName, String destinationDir) throws ZipException {
        unzip(new File(zipName), destinationDir);
    }

    public static void unzip(File zipName, String destinationDir) throws ZipException {
        ZipFile zipFile = new ZipFile(zipName);
        zipFile.extractAll(destinationDir);
    }


    /**
     *
     * @param files 需要压缩的文件
     * @param zipName 文件名称
     * @param pwd 密码
     * @return
     */
    public static void createZip(List<String> files, String zipName, String pwd) {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);
        // Below line is optional. AES 256 is used by default. You can override it to use AES 128. AES 192 is supported only for extracting.
        zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
        List<File> filesToAdd = new ArrayList<>();
        for (String file : files) {
            filesToAdd.add(new File(file));
        }
        // 将文件夹添加到压缩文件
        ZipFile zipFile = new ZipFile(zipName, pwd.toCharArray());
        try {
            zipFile.addFiles(filesToAdd, zipParameters);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }


    /**
     * 压缩文件夹加密
     * @param inputFile  D:\\test  要打包的文件夹
     * @param outputFile  D:\test1.zip 生成的压缩包的名字
     * @param pwd  压缩密码
     */
    public static void zipFileWithPwd(String inputFile,String outputFile, String pwd) {
        // 生成的压缩文件
        try {
            ZipFile zipFile = new ZipFile(outputFile, pwd.toCharArray());
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setEncryptFiles(true);
            zipParameters.setEncryptionMethod(EncryptionMethod.AES);
            zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
            // 要打包的文件夹
            File currentFile = new File(inputFile);
            File[] fs = currentFile.listFiles();
            // 遍历test文件夹下所有的文件、文件夹
            for (File f : fs) {
                if (f.isDirectory()) {
                    zipFile.addFolder(f, zipParameters);
                } else {
                    zipFile.addFile(f, zipParameters);
                }
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
}
