package com.shisfish.news.service.service.system.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.common.constant.UserConstants;
import com.shisfish.news.dao.domain.system.SysDictData;
import com.shisfish.news.dao.domain.system.SysDictType;
import com.shisfish.news.common.exception.CustomException;
import com.shisfish.news.common.utils.DictUtils;
import com.shisfish.news.common.utils.string.StringUtils;
import com.shisfish.news.service.mapper.system.SysDictDataMapper;
import com.shisfish.news.service.mapper.system.SysDictTypeMapper;
import com.shisfish.news.service.service.system.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {

    @Autowired
    private SysDictDataMapper sysDictDataMapper;


    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init() {
        List<SysDictType> dictTypeList = findDictTypeAll();
        // 查询出所有字典类型下的字典数据 存入到redis
        for (SysDictType dictType : dictTypeList) {
            List<SysDictData> dictDatas = sysDictDataMapper.selectList(new LambdaQueryWrapper<SysDictData>()
                    .eq(SysDictData::getStatus, UserConstants.NORMAL)
                    .eq(SysDictData::getDictType, dictType.getDictType())
                    .orderByAsc(SysDictData::getDictSort)
            );
            DictUtils.setDictCache(dictType.getDictType(), dictDatas);
        }
    }


    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    @Override
    public IPage<SysDictType> findPage(Page<SysDictType> sysDictTypePage, SysDictType dictType) {
        return baseMapper.findPage(sysDictTypePage, dictType);
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> findDictDataByType(String dictType) {
        //  从redis中拿出当前字典类型下的字典数据
        List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);
        // 判断有没有数据 有就返回
        if (CollectionUtil.isNotEmpty(dictDatas)) {
            return dictDatas;
        }
        dictDatas = sysDictDataMapper.selectList(new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getStatus, UserConstants.NORMAL)
                .eq(SysDictData::getDictType, dictType)
                .orderByAsc(SysDictData::getDictSort)
        );
        // 如果没有就从数据库中查出来 重新存放
        if (StringUtils.isNotNull(dictDatas)) {
            DictUtils.setDictCache(dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }


    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Override
    public SysDictType findDictTypeById(Long dictId) {
        return getById(dictId);
    }


    /**
     * 新增保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    @Override
    public boolean insertDictType(SysDictType dictType) {
        boolean flag = saveOrUpdate(dictType);
        if (flag) {
            // 清空redis中存放的缓存
            DictUtils.clearDictCache();
        }
        return flag;
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateDictType(SysDictType dictType) {
        // 查询出之前的字典类型
        SysDictType oldDict = getById(dictType.getDictId());
        // 将旧的字典类型名 全部更换
        sysDictDataMapper.updateDictDataType(oldDict.getDictType(), dictType.getDictType());
        // 保存当前字典类型信息
        boolean flag = saveOrUpdate(dictType);
        if (flag) {
            // 清空redis中存放的数据
            DictUtils.clearDictCache();
        }
        return flag;
    }

    /**
     * 批量删除字典信息
     *
     * @param dictIds 需要删除的字典ID
     * @return 结果
     */
    @Override
    public boolean deleteDictTypeByIds(List<Long> dictIds) {
        for (Long dictId : dictIds) {
            SysDictType dictType = findDictTypeById(dictId);
            // 判断当前字典 类型名是否在 字典数据中已经在 使用了
            if (sysDictDataMapper.selectCount(new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getDictType, dictType.getDictType())) > 0) {
                throw new CustomException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
        }
        boolean flag = removeByIds(dictIds);
        if (flag) {
            // 清空redis中存放的数据
            DictUtils.clearDictCache();
        }
        return flag;
    }

    /**
     * 清空缓存数据
     */
    @Override
    public void clearCache() {
        DictUtils.clearDictCache();
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> findDictTypeAll() {
        return list();
    }


    /**
     * 校验字典类型称是否唯一
     *
     * @param dictType 字典类型
     * @return 结果
     */
    @Override
    public String checkDictTypeUnique(SysDictType dictType) {
        // 先判断id是否为空 为空返回-1
        Long dictId = StringUtils.isNull(dictType.getDictId()) ? -1L : dictType.getDictId();
        // 根据字典类型查询出当前字典类型所有数据 进行判断
        SysDictType one = getOne(new LambdaQueryWrapper<SysDictType>().eq(SysDictType::getDictType, dictType.getDictType()));
        // 判断id是否一致
        if (StringUtils.isNotNull(one) && !Objects.equals(one.getDictId(), dictId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
