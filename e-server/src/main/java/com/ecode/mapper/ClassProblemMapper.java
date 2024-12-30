package com.ecode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecode.entity.ClassProblem;
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
public interface ClassProblemMapper extends BaseMapper<ClassProblem> {
    List<Problem> getClassProblem(Integer classId, String name);
}
