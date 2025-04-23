package com.ecode.ai.tools;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ecode.entity.*;
import com.ecode.entity.Class;
import com.ecode.mapper.*;
import com.ecode.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 问题推荐工具类：ai自动调用此工具进行数据库查询
 *
 * @author 竹林听雨
 * @date 2025/04/21
 */
@Component
@Slf4j
public class ProblemRecommendationTools {

    @Autowired
    private StudentClassMapper studentClassMapper;

    @Autowired
    private ClassProblemMapper classProblemMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private ClassScoreMapper classScoreMapper;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserMapper userMapper;

    @Tool(description = "查询学生信息")
    public Map<String, Object> queryStudentInfo(@ToolParam(description = "学生用户id") Integer studentId) {
        Map<String, Object> map = new HashMap<>();
        User user = userMapper.selectById(studentId);
        if (user != null) {
            map.put("用户名", user.getName());
            map.put("学生id", user.getId());
        }
        log.info("查询到的学生信息: {}", map);
        return map;
    }

    @Tool(description = "查询当前学生加入的所有班级")
    public List queryStudentClass(@ToolParam(description = "学生用户id") Integer studentId){
        List<Map<String,Object>> list = new ArrayList<>();
        studentClassMapper.selectList(
                new LambdaQueryWrapper<StudentClass>().eq(StudentClass::getStudentId, studentId)
        ).forEach(sc -> {
            Class c = classMapper.selectById(sc.getClassId());
            Map<String, Object> map = new HashMap<>();
            map.put("班级id", c.getId());
            map.put("班级名称", c.getName());
//            map.put("学生在班内唯一id", sc.getId());
            list.add(map);
        });
        log.info("查询到的班级信息: {}", list);
        return list;
    }

    @Tool(description = "查询当前学生在班内的所有题目")
    public List queryStudentClassProblemCompletion(@ToolParam(description = "学生用户id") Integer studentId, @ToolParam(description = "班级id") Integer classId){
        List<Map<String,Object>> list = new ArrayList<>();

        classProblemMapper.selectList(
                new LambdaQueryWrapper<ClassProblem>().eq(ClassProblem::getClassId, classId)
        ).forEach(cp -> {
            Map<String, Object> map = new HashMap<>();
            ClassScore classScore = classScoreMapper.problemStuInfo(studentId, cp.getId());
            Problem problem = problemMapper.selectOne(
                    new LambdaQueryWrapper<Problem>().eq(Problem::getId, cp.getProblemId()).select(Problem::getTitle)
            );
            map.put("题目id", cp.getProblemId());
            map.put("题目标题", problem.getTitle());
            if (classScore == null || classScore.getPassNumber() == 0) {
                map.put("是否完成", "未完成");
            }else {
                map.put("是否完成", "已完成");
            }
            map.put("题目标签", tagService.getByProblemId(cp.getProblemId()));
            list.add(map);
        });

        log.info("查询到的题目完成情况: {}", list);
        return list;
    }

    @Tool(description = "根据标签id查找班内对应的题目列表")
    public List queryClassProblemsByTagId(@ToolParam(description = "标签id") Integer tagId,
                                          @ToolParam(description = "班级id") Integer classId){
        List<Map<String,Object>> list = new ArrayList<>();
        problemMapper.findProblemsByTagIdAndClassId(tagId, classId).forEach(p -> {
            Map<String, Object> map = new HashMap<>();
            map.put("题目id", p.getId());
            map.put("题目标题", p.getTitle());
            list.add(map);
        });
        log.info("查询到的题目列表: {}", list);
        return list;
    }
}
