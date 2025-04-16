package com.ecode.controller.user.student;

import com.ecode.context.BaseContext;
import com.ecode.dto.ClassProblemPageQueryDTO;
import com.ecode.dto.ClassStudentDTO;
import com.ecode.dto.GeneralPageQueryDTO;
import com.ecode.result.Result;
import com.ecode.service.ClassService;
import com.ecode.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "班级管理")
@RestController
@RequestMapping("/student/class")
@Component("studentClassController")
public class ClassController {
    @Autowired
    private ClassService classService;

    @GetMapping
    @Operation(summary = "加入班级")
    public Result join(@Parameter(name = "邀请码") String invitationCode){
        classService.joinClass(BaseContext.getCurrentId(), invitationCode);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "班级分页查询")
    public Result<PageVO<ClassVO>> page(GeneralPageQueryDTO classPageQueryDTO){
        PageVO<ClassVO> pv = classService.pageQuery(null, BaseContext.getCurrentId(), classPageQueryDTO);
        return Result.success(pv);
    }

    @DeleteMapping
    @Operation(summary = "批量退出班级")
    public Result exit(@Parameter(name = "班级id集合") @RequestParam List<Integer> ids){
        classService.exitBatch(BaseContext.getCurrentId(), ids);
        return Result.success();
    }

    /**
     * 班级题目分页查询
     *
     * @param classProblemPageQueryDTO 类问题页面查询dto
     * @return 结果<页vo < 问题页vo>>
     */
    @GetMapping("/problem/page")
    @Operation(summary = "班级题目分页查询")
    public Result<PageVO<ProblemPageVO>> problemPage(ClassProblemPageQueryDTO classProblemPageQueryDTO){
        PageVO<ProblemPageVO> pv = classService.problemPage(classProblemPageQueryDTO);
        return Result.success(pv);
    }

    @GetMapping("/members/page")
    @Operation(summary = "分页查询指定班级学生列表,含成绩")//todo 权限优化
    public Result<PageVO<UserVO>> studentPage(ClassStudentDTO classStudentDTO){
        PageVO<UserVO> uvs = classService.studentPage(classStudentDTO);
        return Result.success(uvs);
    }

    @GetMapping("/problem/info/{classProblemId}")
    @Operation(summary = "获取班级单个题目的做题详细信息")
    public Result<ProblemStuInfoVO> problemInfo(@PathVariable @Parameter(name = "班级题目id") Integer classProblemId){
        ProblemStuInfoVO psv = classService.problemStuInfo(BaseContext.getCurrentId(), classProblemId);
        return Result.success(psv);
    }

}
