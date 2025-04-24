package com.ecode.mapper;

import com.ecode.dto.AIToolsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AIToolsMapper {

    /**
     * 获取学生的班级信息
     *
     * @param studentId 学生id
     * @return 班级信息列表
     */
    List<AIToolsDTO> selectStudentClass(Integer studentId);

    /**
     * 获取班级的题目信息
     *
     * @param classId 班级id
     * @return 题目信息列表
     */
    List<AIToolsDTO> selectStudentClassProblemCompletion(
            @Param("studentId") Integer studentId,
            @Param("classId") Integer classId
    );

    /**
     * 获取班级的题目标签信息
     *
     * @param classId 班级id
     * @return 题目标签信息列表
     */
    List<AIToolsDTO> selectClassProblemsByTagId(
            @Param("tagId") Integer tagId,
            @Param("classId") Integer classId
    );
}
