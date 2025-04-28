package com.ecode.controller.user.student;

import com.ecode.result.Result;
import com.ecode.service.ClassService;
import com.ecode.service.StatisticService;
import com.ecode.vo.ClassProblemDifficultyNumVO;
import com.ecode.vo.ClassProblemTagNumVO;
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
@RequestMapping("student/statistic")
@Tag(name = "统计操作接口")
@Component("studentStatisticController")
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
