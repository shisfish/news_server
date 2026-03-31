package com.shisfish.news.service.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shisfish.news.dao.domain.system.SysNotice;

import java.util.List;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description:
 * @Version: 1.0.0
 */

public interface ISysNoticeService extends IService<SysNotice> {

    /**
     * 获取公告列表
     *
     * @param page   分页参数
     * @param notice 条件查询对象，notice
     * @return IPage<SysUser>
     */
    IPage<SysNotice> findPage(Page<SysNotice> page, SysNotice notice);


    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    SysNotice findNoticeById(Integer noticeId);


    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    boolean insertNotice(SysNotice notice);

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    boolean updateNotice(SysNotice notice);

    /**
     * 删除公告对象
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    boolean deleteNoticeById(Integer noticeId);

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    boolean deleteNoticeByIds(List<Integer> noticeIds);
}
