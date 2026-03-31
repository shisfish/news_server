package com.shisfish.news.common.utils;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.shisfish.news.common.constant.RedisConstants;
import com.shisfish.news.dao.domain.system.SysDictData;
//import com.shisfish.common.core.redis.RedisService;
import com.shisfish.news.common.utils.string.StringUtils;

import java.util.List;

/**
 * 字典工具类
 */
public class DictUtils {
    /**
     * 设置字典缓存
     *
     * @param key       参数键
     * @param dictDatas 字典数据列表
     */
    public static void setDictCache(String key, List<SysDictData> dictDatas) {
//        SpringUtils.getBean(RedisService.class).setCacheObject(getCacheKey(key), dictDatas);
    }

    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static List<SysDictData> getDictCache(String key) {
//        Object cacheObj = SpringUtils.getBean(RedisService.class).getCacheObject(getCacheKey(key));
//        if (StringUtils.isNotNull(cacheObj)) {
//            return StringUtils.cast(cacheObj);
//        }
        return null;
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @return 字典标签
     */
    public static String getDictLabel(String dictType, String dictValue) {
        if (StringUtils.isNotEmpty(dictType) && StringUtils.isNotEmpty(dictValue)) {
            List<SysDictData> datas = getDictCache(dictType);
            if (CollectionUtils.isNotEmpty(datas)) {
                if (StringUtils.isNotEmpty(datas)) {
                    for (SysDictData dict : datas) {
                        if (dictValue.equals(dict.getDictValue())) {
                            return dict.getDictLabel();
                        }
                    }
                }
            }
        }
        return dictValue;
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @return 字典值
     */
    public static String getDictValue(String dictType, String dictLabel) {
        if (StringUtils.isNotEmpty(dictType) && StringUtils.isNotEmpty(dictLabel)) {
            List<SysDictData> datas = getDictCache(dictType);
            if (CollectionUtils.isNotEmpty(datas)) {
                if (StringUtils.isNotEmpty(datas)) {
                    for (SysDictData dict : datas) {
                        if (dictLabel.equals(dict.getDictLabel())) {
                            return dict.getDictValue();
                        }
                    }
                }
            }
        }
        return dictLabel;
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache() {
//        Collection<String> keys = SpringUtils.getBean(RedisService.class).keys(RedisConstants.SYS_DICT_KEY + "*");
//        SpringUtils.getBean(RedisService.class).deleteObject(keys);
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey) {
        return RedisConstants.SYS_DICT_KEY + configKey;
    }
}
