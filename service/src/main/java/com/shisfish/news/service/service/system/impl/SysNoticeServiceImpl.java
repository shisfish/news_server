package com.shisfish.news.service.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shisfish.news.dao.domain.system.SysNotice;
import com.shisfish.news.service.mapper.system.SysNoticeMapper;
import com.shisfish.news.service.service.system.ISysNoticeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService {
    /**
     * 获取公告列表
     *
     * @param page   分页参数
     * @param notice 条件查询对象，notice
     * @return IPage<SysUser>
     */
    @Override
    public IPage<SysNotice> findPage(Page<SysNotice> page, SysNotice notice) {
        return baseMapper.findPage(page, notice);
    }

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public SysNotice findNoticeById(Integer noticeId) {
        return getById(noticeId);
    }

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public boolean insertNotice(SysNotice notice) {
        return saveOrUpdate(notice);
    }

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public boolean updateNotice(SysNotice notice) {
        return saveOrUpdate(notice);
    }

    /**
     * 删除公告对象
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    @Override
    public boolean deleteNoticeById(Integer noticeId) {
        return removeById(noticeId);
    }

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    @Override
    public boolean deleteNoticeByIds(List<Integer> noticeIds) {
        return removeByIds(noticeIds);
    }
}
