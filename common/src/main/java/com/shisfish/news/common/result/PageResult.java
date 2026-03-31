package com.shisfish.news.common.result;

import com.shisfish.news.common.result.code.ResponseCode;
import lombok.Data;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */
@Data
public class PageResult<T> {
    /**
     * 是否成功
     */
    private boolean flag;
    /**
     * 返回状态码
     */
    private int code;
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 当前页
     */
    private long pageNum;
    /**
     * 页容量
     */
    private long pageSize;
    /**
     * 数据总条数
     */
    private long total;
    /**
     * 返回的数据
     */
    private T rows;

    public PageResult(long pageNum, long pageSize, long total, Object rows) {
        this.flag = true;
        this.code = ResponseCode.OK.getCode();
        this.msg = ResponseCode.OK.getMsg();
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.rows = (T) rows;
    }

    public PageResult(boolean flag, int code, String msg, long pageNum, long pageSize, long total, Object rows) {
        this.flag = flag;
        this.code = code;
        this.msg = msg;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.rows = (T) rows;
    }

    public PageResult(int code, String msg, long pageNum, long pageSize, long total, Object rows) {
        this.flag = true;
        this.msg = msg;
        this.code = code;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.rows = (T) rows;
    }

    public PageResult(String msg, long pageNum, long pageSize, long total, Object rows) {
        this.flag = true;
        this.code = ResponseCode.OK.getCode();
        this.msg = msg;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.rows = (T) rows;
    }
}
