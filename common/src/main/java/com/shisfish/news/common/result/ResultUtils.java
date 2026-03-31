package com.shisfish.news.common.result;

import com.shisfish.news.common.result.code.ResponseCode;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */
public class ResultUtils {

    /////////////////////////////////////// success     ///////////////////////////////////////

    /**
     * 操作成功
     *
     * @return Result<Object>
     */
    public static <T> Result<T> success() {
        return new Result<T>();
    }

    /**
     * 操作成功
     *
     * @param data 自定义数据
     * @return Result<Object>
     */
    public static <T> Result<T> success(Object data) {
        return new Result<T>(data);
    }

    /**
     * 操作成功
     *
     * @param msg 自定义消息
     * @return Result<Object>
     */
    public static <T> Result<T> success(String msg) {
        return new Result<T>(msg);
    }

    /**
     * 操作成功
     *
     * @param code 自定义响应码
     * @param msg  自定义消息
     * @return
     */
    public static <T> Result<T> success(int code, String msg) {
        return new Result<T>(code, msg);
    }

    /**
     * 操作成功
     *
     * @param code 自定义响应码
     * @param msg  自定义消息
     * @param data 自定义数据
     * @return Result<Object>
     */
    public static <T> Result<T> success(int code, String msg, Object data) {
        return new Result<T>(code, msg, data);
    }

    /**
     * 操作成功
     * 分页使用
     *
     * @param code     自定义响应码
     * @param msg      自定义消息
     * @param pageNum  当前页
     * @param pageSize 每页显示数量
     * @param total    总记录数
     * @param rows     数据集合
     * @return PageResult<Object>
     */
    public static <T> PageResult<T> success(int code, String msg, long pageNum, long pageSize, long total, Object rows) {
        return new PageResult<T>(code, msg, pageNum, pageSize, total, rows);
    }

    /**
     * 操作成功
     * 分页使用
     *
     * @param msg      自定义消息
     * @param pageNum  当前页
     * @param pageSize 每页显示数量
     * @param total    总记录数
     * @param rows     数据集合
     * @return PageResult<Object>
     */
    public static <T> PageResult<T> success(String msg, long pageNum, long pageSize, long total, Object rows) {
        return new PageResult<T>(msg, pageNum, pageSize, total, rows);
    }


    /**
     * 操作成功
     * 分页使用
     *
     * @param pageNum  当前页
     * @param pageSize 每页显示数量
     * @param total    总记录数
     * @param rows     数据集合
     * @return PageResult<Object>
     */
    public static <T> PageResult<T> success(long pageNum, long pageSize, long total, Object rows) {
        return new PageResult<T>(pageNum, pageSize, total, rows);
    }

    /////////////////////////////////////// error ///////////////////////////////////////

    /**
     * 操作失败
     *
     * @return Result<Object>
     */
    public static <T> Result<T> error() {
        return new Result<T>(false, ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMsg(), null);
    }

    /**
     * 操作失败
     *
     * @param msg 自定义消息
     * @return Result<Object>
     */
    public static <T> Result<T> error(String msg) {
        return new Result<T>(false, ResponseCode.ERROR.getCode(), msg, null);
    }

    /**
     * 操作失败
     *
     * @param code 自定义响应码
     * @param msg  自定义消息
     * @return Result<Object>
     */
    public static <T> Result<T> error(int code, String msg) {
        return new Result<T>(false, code, msg, null);
    }

    /**
     * 操作失败
     *
     * @param code 自定义响应码
     * @param msg  自定义消息
     * @param data 自定义响应数据
     * @return Result<Object>
     */
    public static <T> Result<T> error(int code, String msg, Object data) {
        return new Result<T>(false, code, msg, data);
    }

}
