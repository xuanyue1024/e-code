package com.ecode.controller.user.admin;

import com.ecode.result.Result;
import com.ecode.service.ClassService;
import com.ecode.vo.ClassDifficultyDistributionVO;
import com.ecode.vo.ClassProblemDifficultyNumVO;
import com.ecode.vo.ClassProblemPassRateVO;
import com.ecode.vo.ClassProblemSubmissionsVO;
import com.ecode.vo.ClassProblemTagNumVO;
import com.ecode.vo.ClassStudentRankVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理员统计接口。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "管理员-统计管理", description = "管理员班级统计数据查询接口")
@RequestMapping("/admin/statistic")
public class AdminStatisticController {

    private final ClassService classService;

    @GetMapping("/problem/submissions/{classId}")
    @Operation(summary = "查询班级题目提交情况", description = "查询指定班级内学生提交次数和通过率")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "查询成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<List<ClassProblemSubmissionsVO>> problemSubmissions(
            @Parameter(name = "classId", description = "班级id", required = true, in = ParameterIn.PATH)
            @PathVariable @NotNull(message = "班级id不能为空") Integer classId) {
        return Result.success(classService.submissionsInfo(classId));
    }

    @GetMapping("/classStudentRank/{classId}")
    @Operation(summary = "查询班级学生成绩排名", description = "查询指定班级内学生成绩排名")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "查询成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<List<ClassStudentRankVO>> getClassStudentRank(
            @Parameter(name = "classId", description = "班级id", required = true, in = ParameterIn.PATH)
            @PathVariable @NotNull(message = "班级id不能为空") Integer classId) {
        return Result.success(classService.getClassStudentRank(classId));
    }

    @GetMapping("/classStudentDifficulty/{classId}")
    @Operation(summary = "查询学生难度完成分布", description = "查询指定班级内学生完成不同难度题目的分布")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "查询成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<List<ClassDifficultyDistributionVO>> getClassStudentDifficulty(
            @Parameter(name = "classId", description = "班级id", required = true, in = ParameterIn.PATH)
            @PathVariable @NotNull(message = "班级id不能为空") Integer classId) {
        return Result.success(classService.getClassStudentDifficulty(classId));
    }

    @GetMapping("/classProblemPassRate/{classId}")
    @Operation(summary = "查询班级题目通过率排行", description = "查询指定班级内题目通过率排行")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "查询成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<List<ClassProblemPassRateVO>> getClassProblemPassRate(
            @Parameter(name = "classId", description = "班级id", required = true, in = ParameterIn.PATH)
            @PathVariable @NotNull(message = "班级id不能为空") Integer classId) {
        return Result.success(classService.getClassProblemPassRate(classId));
    }

    @GetMapping("/classProblemDifficultyNum/{classId}")
    @Operation(summary = "查询班级题目难度分布", description = "查询指定班级题目难度数量")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "查询成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<List<ClassProblemDifficultyNumVO>> getClassProblemDifficultyNum(
            @Parameter(name = "classId", description = "班级id", required = true, in = ParameterIn.PATH)
            @PathVariable @NotNull(message = "班级id不能为空") Integer classId) {
        return Result.success(classService.getClassProblemDifficultyNum(classId));
    }

    @GetMapping("/classProblemTagNum/{classId}")
    @Operation(summary = "查询班级题目标签分布", description = "查询指定班级内各标签题目数量")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "查询成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<List<ClassProblemTagNumVO>> getClassProblemTagNum(
            @Parameter(name = "classId", description = "班级id", required = true, in = ParameterIn.PATH)
            @PathVariable @NotNull(message = "班级id不能为空") Integer classId) {
        return Result.success(classService.getClassProblemTagNum(classId));
    }
}
