package com.ecode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecode.entity.Problem;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 竹林听雨
 * @since 2024-12-28
 */
public interface ProblemMapper extends BaseMapper<Problem> {

    /**
     * 根据标签ID和班级ID查询班内问题列表
     *
     * @param tagId   标签ID
     * @param classId 班级ID
     * @return 符合条件的问题列表
     */
    List<Problem> findProblemsByTagIdAndClassId(Integer tagId, Integer classId);
}
