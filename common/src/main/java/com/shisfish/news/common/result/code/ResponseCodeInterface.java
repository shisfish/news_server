package com.shisfish.news.common.result.code;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: code接口
 * @Version: 1.0.0
 */
public interface ResponseCodeInterface {


    /**
     * 获取 响应消息
     *
     * @return String
     */
    String getMsg();

    /**
     * 获取响应码
     *
     * @return int
     */
    int getCode();
}
