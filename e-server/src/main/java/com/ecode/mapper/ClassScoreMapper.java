package com.ecode.mapper;

import com.ecode.entity.ClassScore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 竹林听雨
 * @since 2024-12-28
 */
public interface ClassScoreMapper extends BaseMapper<ClassScore> {

    ClassScore problemStuInfo(Integer stuId, Integer classProblemId);
}
