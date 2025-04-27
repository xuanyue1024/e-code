package com.ecode.controller.user.teacher;

import com.ecode.result.Result;
import com.ecode.service.ClassService;
import com.ecode.service.StatisticService;
import com.ecode.vo.ClassDifficultyDistributionVO;
import com.ecode.vo.ClassProblemSubmissionsVO;
import com.ecode.vo.ClassStudentRankVO;
import com.ecode.vo.HistogramVO;
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
    private StatisticService statisticService;

    @Autowired
    private ClassService classService;

    @GetMapping("/dateCreateUser")
    @Operation(summary = "指定每道题的得分数")
    public Result<HistogramVO> getScore(@Parameter(name = "班级id") Integer classId){
        HistogramVO hv = statisticService.getScore(classId);
        return Result.success(hv);
    }


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
     * 班级内学生成绩排名
     * @param classId 班级ID
     * @return 班级学生成绩排名
     */
    @GetMapping("/class/student/rank/{classId}")
    @Operation(summary = "班级学生成绩排名")
    public Result<List<ClassStudentRankVO>> getClassStudentRank(@PathVariable @Parameter(name = "班级id") Integer classId) {
        return Result.success(classService.getClassStudentRank(classId));
    }
    
    /**
     * 获取班级题目难度分布
     * @param classId 班级ID
     * @return 难度分布结果
     */
    @GetMapping("/class/difficulty/distribution/{classId}")
    @Operation(summary = "班级题目难度分布")
    public Result<List<ClassDifficultyDistributionVO>> getClassDifficultyDistribution(@PathVariable @Parameter(name = "班级id") Integer classId) {
        return Result.success(classService.getClassDifficultyDistribution(classId));
    }
}
