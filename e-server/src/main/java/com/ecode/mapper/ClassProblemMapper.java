package com.ecode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecode.entity.ClassProblem;
import com.ecode.vo.ProblemPageVO;

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
    List<ProblemPageVO> getClassProblem(Integer classId, String name);
}
