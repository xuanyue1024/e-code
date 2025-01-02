package com.ecode.controller.user.student;

import com.ecode.result.Result;
import com.ecode.service.StatisticService;
import com.ecode.vo.HistogramVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("student/statistic")
@Api(tags = "统计操作接口")
@Component("studentStatisticController")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @GetMapping("/dateCreateUser")
    @ApiOperation("指定每道题的得分数")
    public Result<HistogramVO> getScore(@ApiParam("班级id") Integer classId){
        HistogramVO hv = statisticService.getScore(classId);
        return Result.success(hv);
    }
}
