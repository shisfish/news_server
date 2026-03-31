package com.shisfish.news.service.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.common.constant.RedisConstants;
import com.shisfish.news.common.constant.UserConstants;
//import com.shisfish.common.core.redis.RedisService;
import com.shisfish.news.common.utils.string.StringUtils;
import com.shisfish.news.dao.domain.system.SysConfig;
import com.shisfish.news.service.mapper.system.SysConfigMapper;
import com.shisfish.news.service.service.system.ISysConfigService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

//    @Autowired
//    private RedisService redisService;

    /**
     * 项目启动时，初始化参数到缓存
     */
//    @PostConstruct
//    public void init() {
//        List<SysConfig> configsList = list();
//        for (SysConfig config : configsList) {
//            // 存入配置参数 到redis
//            redisService.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
//        }
//    }


    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public IPage<SysConfig> findPage(Page<SysConfig> sysConfigPage, SysConfig config) {
        return baseMapper.findPage(sysConfigPage, config);
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    public SysConfig findConfigById(Integer configId) {
        return getById(configId);
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    @Override
    public String findConfigByKey(String configKey) {
        // 从redis中拿出配置信息
//        String configValue = Convert.toStr(redisService.getCacheObject(getCacheKey(configKey)));
//        if (StringUtils.isNotEmpty(configValue)) {
//            return configValue;
//        }
        // 根据 参数键名 查询当前配置信息
        SysConfig one = getOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, configKey));
        if (StringUtils.isNotNull(one)) {
//            redisService.setCacheObject(getCacheKey(configKey), one.getConfigValue());
            return one.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public boolean insertConfig(SysConfig config) {
        boolean flag = saveOrUpdate(config);
        if (flag) {
            // 重新存入数据库
//            redisService.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return flag;
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public boolean updateConfig(SysConfig config) {
        boolean flag = saveOrUpdate(config);
        if (flag) {
            // 重新存入数据库
//            redisService.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return flag;
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     * @return 结果
     */
    @Override
    public boolean deleteConfigByIds(List<Integer> configIds) {
        boolean flag = removeByIds(configIds);
        if (flag) {
//            // 拿到所有 配置信息
//            Collection<String> keys = redisService.keys(RedisConstants.SYS_CONFIG_KEY + "*");
//            // 删除所有
//            redisService.deleteObject(keys);
        }
        return flag;
    }

    /**
     * 清空缓存数据
     */
    @Override
    public void clearCache() {
//        Collection<String> keys = redisService.keys(RedisConstants.SYS_CONFIG_KEY + "*");
//        redisService.deleteObject(keys);
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public String checkConfigKeyUnique(SysConfig config) {
        Integer configId = StringUtils.isNull(config.getConfigId()) ? -1 : config.getConfigId();
        SysConfig info = getOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, config.getConfigKey()));
        if (StringUtils.isNotNull(info) && !Objects.equals(info.getConfigId(), configId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return RedisConstants.SYS_CONFIG_KEY + configKey;
    }
}
