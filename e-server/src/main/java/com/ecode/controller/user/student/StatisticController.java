package com.ecode.controller.user.student;

import com.ecode.result.Result;
import com.ecode.service.StatisticService;
import com.ecode.vo.HistogramVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("student/statistic")
@Tag(name = "统计操作接口")
@Component("studentStatisticController")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @GetMapping("/dateCreateUser")
    @Operation(summary = "指定每道题的得分数")
    public Result<HistogramVO> getScore(@Parameter(name = "班级id") Integer classId){
        HistogramVO hv = statisticService.getScore(classId);
        return Result.success(hv);
    }
}
