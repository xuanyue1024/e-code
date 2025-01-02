package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.entity.Class;
import com.ecode.entity.ClassProblem;
import com.ecode.entity.ClassScore;
import com.ecode.entity.StudentClass;
import com.ecode.enumeration.UserRole;
import com.ecode.exception.ClassException;
import com.ecode.mapper.*;
import com.ecode.service.StatisticService;
import com.ecode.vo.HistogramVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private StudentClassMapper studentClassMapper;
    @Autowired
    private ClassScoreMapper classScoreMapper;
    @Autowired
    private ClassProblemMapper classProblemMapper;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ClassMapper classMapper;
    @Override
    public HistogramVO getScore(Integer classId) {
        verifyClassStudent(classId);
        List<ClassProblem> classProblems = classProblemMapper.selectList(new LambdaQueryWrapper<ClassProblem>().eq(ClassProblem::getClassId, classId));
        List<String> problemNameList = new ArrayList<>();
        List<Integer> submitList = new ArrayList<>();
        List<Integer> passList = new ArrayList<>();
        List<Integer> scoreList = new ArrayList<>();
        classProblems.forEach(cp -> {
            ClassScore cs = classScoreMapper.problemStuInfo(BaseContext.getCurrentId(),cp.getId());
            if (cs == null){
                cs = ClassScore.builder()
                        .submitNumber(0)
                        .passNumber(0)
                        .score(0)
                        .build();
            }
            problemNameList.add(problemMapper.selectById(cp.getProblemId()).getTitle());
            submitList.add(cs.getSubmitNumber());
            passList.add(cs.getPassNumber());
            scoreList.add(cs.getScore());
        });
        Map<String,String> map = new HashMap<>();
        map.put("submit",StringUtils.join(submitList, ","));
        map.put("pass", StringUtils.join(passList, ","));
        map.put("score", StringUtils.join(scoreList, ","));

        return HistogramVO.builder()
                .key(StringUtils.join(problemNameList, ","))
                .value(map)
                .build();
    }
    /**
     * 验证教师是否在指定班级授课
     * 验证学生是否在指定班级
     *
     * @param classId 班级ID
     */
    private void verifyClassStudent(Integer classId){
        //获取当前用户id与用户角色
        Integer currentId = BaseContext.getCurrentId();
        UserRole currentRole = BaseContext.getCurrentRole();

        switch (currentRole){
            case STUDENT: {//学生
                StudentClass studentClass = studentClassMapper.selectOne(new LambdaQueryWrapper<StudentClass>()
                        .eq(StudentClass::getStudentId, currentId)
                        .eq(StudentClass::getClassId, classId)
                );
                // 如果查询结果为空，说明指定的学生与班级关联不存在，抛出异常
                if (studentClass == null){
                    throw new ClassException(MessageConstant.CLASS_AND_STUDENT_NOT_FOUND);
                }
                break;
            }
            case TEACHER:{//老师
                // 查询指定班级ID和教师ID的班级信息，以验证教师与班级的关联
                com.ecode.entity.Class c = classMapper.selectOne(new LambdaQueryWrapper<com.ecode.entity.Class>()
                        .eq(com.ecode.entity.Class::getId, classId)
                        .eq(Class::getTeacherId, currentId)
                );

                // 如果查询结果为空，说明指定的教师与班级关联不存在，抛出异常
                if (c == null){
                    throw new ClassException(MessageConstant.CLASS_AND_TEACHER_NOT_FOUND);
                }
                break;
            }
        }

    }
}
