package com.shisfish.news.common.utils;

/**
 * @author shisfish
 * @date 2023/9/18
 * @Description 遮盖文字
 */
public class MaskUtil {

    /**
     * 需要替换的内容
     */
    private static final String MASK = "[^-()]";

    /**
     * 替换之后的内容
     */
    private static final String MASK_CHAR = "*";

    /**
     * 遮盖
     *
     * @param str   替换的对象
     * @param begin 开始左表 从0开始
     * @param size  遮盖的长度
     * @return
     */
    public static String mask(String str, int begin, int size) {
        int length = str.length();
        int end = begin + size;
        if (end > length) {
            end = length;
        }
        return str.substring(0, begin) + str.substring(begin, end).replaceAll(MASK, MASK_CHAR) + str.substring(end);
    }

}
