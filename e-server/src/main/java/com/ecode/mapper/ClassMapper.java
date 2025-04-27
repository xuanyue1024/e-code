package com.ecode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecode.entity.Class;
import com.ecode.vo.ClassDifficultyDistributionVO;
import com.ecode.vo.ClassProblemSubmissionsVO;
import com.ecode.vo.ClassStudentRankVO;
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
     * 根据班级id查询班级题目提交情况
     * @param classId
     * @return
     */
    List<ClassProblemSubmissionsVO> selectClassProblemSubmissionsByClassId(Integer classId);


    /**
     * 查询班级内学生成绩排名
     * @param classId 班级ID
     * @return 学生成绩排名列表
     */
    List<ClassStudentRankVO> selectClassStudentRank(Integer classId);
    
    /**
     * 查询班级题目难度分布
     * @param classId 班级ID
     * @return 难度分布列表
     */
    List<ClassDifficultyDistributionVO> selectClassDifficultyDistribution(Integer classId);
}
