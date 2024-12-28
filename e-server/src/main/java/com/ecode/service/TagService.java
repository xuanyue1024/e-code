package com.ecode.service;

import com.ecode.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecode.vo.TagVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 竹林听雨
 * @since 2024-12-28
 */
public interface TagService extends IService<Tag> {

    /**
     * 按名字模糊查询
     *
     * @param name 名字
     * @return 列表<tag vo>
     */
    List<TagVO> getByName(String name);
}
