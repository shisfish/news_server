package com.shisfish.news.service.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.dao.domain.system.SysDictData;
import com.shisfish.news.common.utils.DictUtils;
import com.shisfish.news.service.mapper.system.SysDictDataMapper;
import com.shisfish.news.service.service.system.ISysDictDataService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {


    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public IPage<SysDictData> findPage(Page<SysDictData> sysDictDataPage, SysDictData dictData) {
        return baseMapper.findPage(sysDictDataPage, dictData);
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public SysDictData findDictDataById(Long dictCode) {
        return getById(dictCode);
    }

    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public boolean insertDictData(SysDictData dictData) {
        boolean flag = saveOrUpdate(dictData);
        if (flag) {
            // 清空缓存
            DictUtils.clearDictCache();
        }
        return flag;
    }

    /**
     * 修改保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */

    @Override
    public boolean updateDictData(SysDictData dictData) {
        boolean flag = saveOrUpdate(dictData);
        if (flag) {
            // 清空缓存
            DictUtils.clearDictCache();
        }
        return flag;
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     * @return 结果
     */
    @Override
    public boolean deleteDictDataByIds(List<Long> dictCodes) {
        boolean flag = removeByIds(dictCodes);
        if (flag) {
            // 清空缓存
            DictUtils.clearDictCache();
        }
        return flag;
    }
}
