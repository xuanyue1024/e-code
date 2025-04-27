package com.ecode.ai.tools;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ecode.entity.ClassProblem;
import com.ecode.entity.ClassScore;
import com.ecode.entity.Problem;
import com.ecode.entity.User;
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
    private AIToolsMapper aiToolsMapper;

    @Autowired
    private ClassProblemMapper classProblemMapper;

    @Autowired
    private ClassScoreMapper classScoreMapper;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserMapper userMapper;

    @Tool(description = "查询用户信息")
    public Map<String, Object> queryStudentInfo(@ToolParam(description = "用户id") Integer studentId) {
        Map<String, Object> map = new HashMap<>();
        User user = userMapper.selectById(studentId);
        if (user != null) {
            map.put("用户名", user.getName());
            map.put("学生用户id", user.getId());
            map.put("用户角色", user.getRole());
        }
        log.info("查询到的学生信息: {}", map);
        return map;
    }

    @Tool(description = "查询当前学生加入的所有班级")
    public List<Map<String, Object>> queryStudentClass(@ToolParam(description = "学生用户id") Integer studentId) {
        List<Map<String,Object>> list = new ArrayList<>();
        aiToolsMapper.selectStudentClass(studentId).forEach(sc -> {
            Map<String, Object> map = new HashMap<>();
            map.put("班级id", sc.getClassId());
            map.put("班级名称", sc.getClassName());
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
                    new LambdaQueryWrapper<Problem>().eq(Problem::getId, cp.getProblemId()).select(Problem::getTitle, Problem::getGrade)
            );
            map.put("题目id", cp.getProblemId());
            map.put("题目标题", problem.getTitle());
            map.put("题目难度", problem.getGrade().getValue() + 1);
            if (classScore == null || classScore.getPassNumber() < 4) {
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
    public List queryClassProblemsByTagId(
            @ToolParam(description = "标签id") Integer tagId,
            @ToolParam(description = "班级id") Integer classId) {
        List<Map<String, Object>> list = new ArrayList<>();
        aiToolsMapper.selectClassProblemsByTagId(tagId, classId).forEach(at -> {
            Map<String, Object> map = new HashMap<>();
            map.put("题目id", at.getProblemId());
            map.put("题目标题", at.getProblemTitle());
            map.put("题目难度", at.getProblemGrade() + 1);
            list.add(map);
        });
        log.info("查询到的题目列表: {}", list);
        return list;
    }
}
