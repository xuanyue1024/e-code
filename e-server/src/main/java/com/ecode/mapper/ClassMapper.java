package com.ecode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecode.entity.Class;
import com.ecode.vo.ClassProblemCompleteVO;
import com.ecode.vo.ClassProblemSubmissionsVO;
import com.ecode.vo.ClassVO;

import java.util.List;

/**
 * 班级mapper接口
 *
 * @author 竹林听雨
 * @date 2024/11/24
 */
public interface ClassMapper extends BaseMapper<Class> {
    List<ClassVO> pageQueryByName(String name,Integer teacherId,Integer studentId, String order, Boolean isAsc);

    /**
     * 根据班级id查询班级完成情况
     * @param classId 班级id
     * @return 班级完成情况
     */
    ClassProblemCompleteVO selectClassProblemCompleteByClassId(Integer classId);

    /**
     * 根据班级id查询班级题目提交情况
     * @param classId
     * @return
     */
    List<ClassProblemSubmissionsVO> selectClassProblemSubmissionsByClassId(Integer classId);
}
