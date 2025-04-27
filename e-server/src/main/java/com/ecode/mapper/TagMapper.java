package com.ecode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecode.entity.Tag;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 竹林听雨
 * @since 2024-12-28
 */
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据题目ID查询关联标签
     *
     * @param problemId 题目ID
     * @return 关联的标签列表
     */
    List<Tag> selectTagByProblemId(Integer problemId);
}
