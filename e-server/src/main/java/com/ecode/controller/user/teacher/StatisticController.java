package com.ecode.controller.user.teacher;

import com.ecode.result.Result;
import com.ecode.service.ClassService;
import com.ecode.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("teacher/statistic")
@Tag(name = "统计操作接口")
@Component("teacherStatisticController")
public class StatisticController {

    @Autowired
    private ClassService classService;

    /**
     * 班级题目提交情况表
     * @param classId 班级id
     * @return Result<ClassProblemSubmissionsVO>
     */
    @GetMapping("/problem/submissions/{classId}")
    @Operation(summary = "班级题目提交情况表")
    public Result<List<ClassProblemSubmissionsVO>> problemSubmissions(@PathVariable @Parameter(name = "班级id") Integer classId){

        // 返回提交的题目数量
        return Result.success(classService.submissionsInfo(classId));
    }


    /**
     * 班级学生成绩排名
     * @param classId 班级id
     * @return Result<ClassStudentRankVO>
     */
    @GetMapping("/classStudentRank/{classId}")
    @Operation(summary = "获取班级学生成绩排名")
    public Result<List<ClassStudentRankVO>> getClassStudentRank(@PathVariable @Parameter(name = "班级id") Integer classId) {
        List<ClassStudentRankVO> rankList = classService.getClassStudentRank(classId);
        return Result.success(rankList);
    }

    /**
     * 班级学生完成不同难度题目分布
     * @param classId 班级id
     * @return Result<ClassDifficultyDistributionVO>
     */
    @GetMapping("/classStudentDifficulty/{classId}")
    @Operation(summary = "获取班级学生完成不同难度题目分布")
    public Result<List<ClassDifficultyDistributionVO>> getClassStudentDifficulty(@PathVariable @Parameter(name = "班级id") Integer classId) {
        List<ClassDifficultyDistributionVO> difficultyList = classService.getClassStudentDifficulty(classId);
        return Result.success(difficultyList);
    }


    /**
     * 班级题目通过率排行榜
     * @param classId 班级id
     * @return Result<ClassProblemPassRateVO>
     */
    @GetMapping("/classProblemPassRate/{classId}")
    @Operation(summary = "获取班级题目通过率排行榜")
    public Result<List<ClassProblemPassRateVO>> getClassProblemPassRate(@PathVariable @Parameter(name = "班级id") Integer classId) {
        List<ClassProblemPassRateVO> passRateList = classService.getClassProblemPassRate(classId);
        return Result.success(passRateList);
    }

    /**
     * 获取指定班级的题目难度分布情况。
     *
     * @param classId 班级ID
     * @return 返回封装了班级题目难度分布数据的Result对象
     */
    @GetMapping("/classProblemDifficultyNum/{classId}")
    @Operation(summary = "获取班级题目难度分布")
    public Result<List<ClassProblemDifficultyNumVO>> getClassProblemDifficultyNum(@PathVariable @Parameter(name = "班级id") Integer classId) {
        List<ClassProblemDifficultyNumVO> difficultyNumList = classService.getClassProblemDifficultyNum(classId);
        return Result.success(difficultyNumList);
    }

    /**
     * 获取班级题目标签数量
     *
     * @param classId 班级ID
     * @return 返回封装了班级题目标签数量数据的Result对象
     */
    @GetMapping("/classProblemTagNum/{classId}")
    @Operation(summary = "获取班内标签的题目数量")
    public Result<List<ClassProblemTagNumVO>> getClassProblemTagNum(@PathVariable @Parameter(name = "班级id") Integer classId) {
        List<ClassProblemTagNumVO> tagNumList = classService.getClassProblemTagNum(classId);
        return Result.success(tagNumList);
    }
}
