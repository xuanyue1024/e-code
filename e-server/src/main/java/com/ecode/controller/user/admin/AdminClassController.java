package com.ecode.controller.user.admin;

import com.ecode.dto.AdminClassCreateDTO;
import com.ecode.dto.AdminClassStudentDTO;
import com.ecode.dto.AdminClassUpdateDTO;
import com.ecode.dto.ClassProblemDTO;
import com.ecode.dto.ClassProblemPageQueryDTO;
import com.ecode.dto.ClassStudentDTO;
import com.ecode.dto.GeneralPageQueryDTO;
import com.ecode.entity.Class;
import com.ecode.result.Result;
import com.ecode.service.ClassService;
import com.ecode.vo.ClassVO;
import com.ecode.vo.ImportResultVO;
import com.ecode.vo.PageVO;
import com.ecode.vo.ProblemPageVO;
import com.ecode.vo.ProblemStuInfoVO;
import com.ecode.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 管理员班级管理接口。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "管理员-班级管理", description = "管理员全局班级、班级题目和班级成员管理接口")
@RequestMapping("/admin/class")
public class AdminClassController {

    private final ClassService classService;

    @PostMapping
    @Operation(summary = "新增班级", description = "管理员创建班级，必须指定授课教师")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "新增成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<Void> add(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "班级创建参数", required = true)
            @RequestBody @Valid AdminClassCreateDTO createDTO) {
        classService.adminAddClass(createDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询班级", description = "管理员全局分页查询班级，不限制教师或学生归属")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "查询成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<PageVO<ClassVO>> page(@ParameterObject @Valid GeneralPageQueryDTO queryDTO) {
        return Result.success(classService.pageQuery(null, null, queryDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询班级详情", description = "根据班级id查询班级详情")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "查询成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<Class> get(
            @Parameter(name = "id", description = "班级id", required = true, in = ParameterIn.PATH)
            @PathVariable @NotNull(message = "班级id不能为空") Integer id) {
        return Result.success(classService.getAdminClassById(id));
    }

    @PutMapping
    @Operation(summary = "更新班级", description = "管理员更新班级名称和授课教师")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "更新成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<Void> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "班级更新参数", required = true)
            @RequestBody @Valid AdminClassUpdateDTO updateDTO) {
        classService.adminUpdateClass(updateDTO);
        return Result.success();
    }

    @DeleteMapping
    @Operation(summary = "批量删除班级", description = "管理员批量删除班级及班级题目、成员关系")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "删除成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<Void> delete(
            @Parameter(name = "ids", description = "班级id集合", required = true, in = ParameterIn.QUERY)
            @RequestParam @NotEmpty(message = "班级id集合不能为空") List<Integer> ids) {
        classService.adminDeleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/problem")
    @Operation(summary = "班级批量添加题目", description = "管理员为指定班级批量添加题目")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "添加成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<Void> addProblemBatch(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "班级题目添加参数", required = true)
            @RequestBody @Valid ClassProblemDTO classProblemDTO) {
        classService.addProblemBatch(classProblemDTO);
        return Result.success();
    }

    @DeleteMapping("/problem")
    @Operation(summary = "班级批量移除题目", description = "管理员从指定班级批量移除题目")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "移除成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<Void> deleteProblemBatch(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "班级题目移除参数", required = true)
            @RequestBody @Valid ClassProblemDTO classProblemDTO) {
        classService.deleteProblemBatch(classProblemDTO);
        return Result.success();
    }

    @GetMapping("/problem/page")
    @Operation(summary = "分页查询班级题目", description = "分页查询指定班级题目")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "查询成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<PageVO<ProblemPageVO>> problemPage(@ParameterObject @Valid ClassProblemPageQueryDTO queryDTO) {
        return Result.success(classService.problemPage(queryDTO));
    }

    @GetMapping("/members/page")
    @Operation(summary = "分页查询班级学生", description = "分页查询指定班级学生列表和成绩")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "查询成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<PageVO<UserVO>> studentPage(@ParameterObject @Valid ClassStudentDTO classStudentDTO) {
        return Result.success(classService.studentPage(classStudentDTO));
    }

    @PostMapping("/member")
    @Operation(summary = "批量添加班级学生", description = "管理员向指定班级批量添加学生")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "添加成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<Void> addStudents(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "班级学生添加参数", required = true)
            @RequestBody @Valid AdminClassStudentDTO dto) {
        classService.adminAddStudents(dto);
        return Result.success();
    }

    @DeleteMapping("/member")
    @Operation(summary = "批量移除班级学生", description = "管理员从指定班级批量移除学生")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "移除成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<Void> removeStudents(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "班级学生移除参数", required = true)
            @RequestBody @Valid AdminClassStudentDTO dto) {
        classService.adminRemoveStudents(dto);
        return Result.success();
    }

    @GetMapping("/problem/info/{classProblemId}/{studentId}")
    @Operation(summary = "查询学生题目作答详情", description = "查询指定学生在指定班级题目上的作答详情")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "查询成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<ProblemStuInfoVO> problemInfo(
            @Parameter(name = "classProblemId", description = "班级题目id", required = true, in = ParameterIn.PATH)
            @PathVariable @NotNull(message = "班级题目id不能为空") Integer classProblemId,
            @Parameter(name = "studentId", description = "学生id", required = true, in = ParameterIn.PATH)
            @PathVariable @NotNull(message = "学生id不能为空") Integer studentId) {
        return Result.success(classService.problemStuInfo(studentId, classProblemId));
    }

    @GetMapping("/export")
    @Operation(summary = "导出班级", description = "导出班级 Excel")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "导出成功",
            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    schema = @Schema(type = "string", format = "binary"))))
    public ResponseEntity<byte[]> exportClasses() {
        return excelResponse("班级导出.xlsx", classService.exportClasses());
    }

    @GetMapping("/import/template")
    @Operation(summary = "下载班级导入模板", description = "下载班级 Excel 导入模板")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "下载成功",
            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    schema = @Schema(type = "string", format = "binary"))))
    public ResponseEntity<byte[]> exportClassTemplate() {
        return excelResponse("班级导入模板.xlsx", classService.exportClassTemplate());
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "导入班级", description = "导入班级 Excel，id、邀请码或 teacherId+name 已存在时跳过并报告")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "导入完成",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<ImportResultVO> importClasses(
            @Parameter(name = "file", description = "班级 Excel 文件，仅支持 .xlsx", required = true)
            @RequestParam("file") MultipartFile file) {
        return Result.success(classService.importClasses(file));
    }

    private ResponseEntity<byte[]> excelResponse(String fileName, byte[] data) {
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                .body(data);
    }
}
