package com.ecode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecode.entity.Class;
import com.ecode.vo.*;

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
     * 根据班级id查询班级题目提交情况
     * @param classId
     * @return
     */
    List<ClassProblemSubmissionsVO> selectClassProblemSubmissionsByClassId(Integer classId);

    /**
     * 查询班级学生成绩排名
     * @param classId 班级ID
     * @return 学生排名列表
     */
    List<ClassStudentRankVO> selectClassStudentRank(Integer classId);

    /**
     * 查询班级学生完成不同难度题目分布
     * @param classId 班级ID
     * @return 学生难度分布列表
     */
    List<ClassDifficultyDistributionVO> selectClassStudentDifficulty(Integer classId);

    /**
     * 查询班级题目通过率排行榜
     * @param classId 班级ID
     * @return 题目通过率列表
     */
    List<ClassProblemPassRateVO> selectClassProblemPassRate(Integer classId);

    /**
     * 查询班级题目难度数量
     * @param classId 班级ID
     * @return 班级题目完成情况列表
     */
    List<ClassProblemDifficultyNumVO> getDifficultyNum(Integer classId);

    /**
     * 查询班级题目标签数量
     * @param classId 班级ID
     * @return 班级题目标签数量列表
     */
    List<ClassProblemTagNumVO> getClassProblemTagNum(Integer classId);

}
