package com.ecode.controller.user.teacher;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "题目管理")
@RestController
@RequestMapping("/teacher/problem")
@Component("teacherProblemController")
public class ProblemController {

}
