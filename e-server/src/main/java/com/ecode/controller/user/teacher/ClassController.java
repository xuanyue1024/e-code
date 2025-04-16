package com.ecode.controller.user.teacher;

import com.ecode.context.BaseContext;
import com.ecode.dto.ClassProblemDTO;
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
@RequestMapping("/teacher/class")
@Component("teacherClassController")
public class ClassController {

    @Autowired
    private ClassService classService;

    @PostMapping
    @Operation(summary = "增加班级")
    public Result add(String name){
        classService.addClass(name);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "班级分页查询")
    public Result<PageVO<ClassVO>> page(GeneralPageQueryDTO generalPageQueryDTO){
        PageVO<ClassVO> pv = classService.pageQuery(BaseContext.getCurrentId(), null, generalPageQueryDTO);
        return Result.success(pv);
    }

    @DeleteMapping
    @Operation(summary = "批量删除班级")
    public Result delete(@Parameter(name = "班级id集合") @RequestParam List<Integer> ids){
        classService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "修改班级信息")
    public Result update(@Parameter(name = "班级id") Integer id,
                         @Parameter(name = "班级名称") String name){
        classService.updateNameByIdAndTeacherId(id, BaseContext.getCurrentId(), name);
        return Result.success();
    }

    /**
     * 为班级增加题目
     *
     * @param classProblemDTO 类添加问题
     * @return 后端统一返回结果
     */
    @PostMapping("/problem")
    @Operation(summary = "班级增加题目")
    public Result addProblemBatch(@RequestBody ClassProblemDTO classProblemDTO){
        classService.addProblemBatch(classProblemDTO);
        return Result.success();
    }

    /**
     * 批量删除班级题目
     *
     * @param classProblemDTO 类问题d
     * @return 后端统一返回结果
     */
    @DeleteMapping("/problem")
    @Operation(summary = "批量移除班级题目")
    public Result deleteProblemBatch(@RequestBody ClassProblemDTO classProblemDTO){
        classService.deleteProblemBatch(classProblemDTO);
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

    @GetMapping("/problem/info/{classProblemId}/{studentId}")
    @Operation(summary = "获取班级单个题目的做题详细信息")
    public Result<ProblemStuInfoVO> problemInfo(@PathVariable @Parameter(name = "班级题目id") Integer classProblemId,
                                                @PathVariable @Parameter(name = "学生id") Integer studentId){
        ProblemStuInfoVO psv = classService.problemStuInfo(studentId, classProblemId);
        return Result.success(psv);
    }
}
