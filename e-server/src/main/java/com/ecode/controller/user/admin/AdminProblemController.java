package com.ecode.controller.user.admin;

import com.ecode.dto.GeneralPageQueryDTO;
import com.ecode.dto.ProblemAddDTO;
import com.ecode.dto.ProblemUpdateDTO;
import com.ecode.dto.SetTagsDTO;
import com.ecode.result.Result;
import com.ecode.service.ProblemService;
import com.ecode.vo.PageVO;
import com.ecode.vo.ProblemEditVO;
import com.ecode.vo.ImportResultVO;
import com.ecode.vo.ProblemPageVO;
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
 * 管理员题目管理接口。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "管理员-题目管理", description = "管理员题目增删改查与标签设置接口")
@RequestMapping("/admin/problem")
public class AdminProblemController {

    private final ProblemService problemService;

    @PostMapping
    @Operation(summary = "新增题目", description = "新增题目并返回题目id")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "新增成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<Integer> add(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "题目新增参数", required = true)
            @RequestBody @Valid ProblemAddDTO problemAddDTO) {
        return Result.success(problemService.add(problemAddDTO));
    }

    @DeleteMapping
    @Operation(summary = "批量删除题目", description = "根据题目id集合批量删除题目")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "删除成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<Void> deleteBatch(
            @Parameter(name = "ids", description = "题目id集合", required = true, in = ParameterIn.QUERY)
            @RequestParam @NotEmpty(message = "题目id集合不能为空") List<Integer> ids) {
        problemService.deleteProblemBatch(ids);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "修改题目信息", description = "根据题目id修改题目信息")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "修改成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<Void> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "题目更新参数", required = true)
            @RequestBody @Valid ProblemUpdateDTO problemUpdateDTO) {
        problemService.updateProblem(problemUpdateDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询题目详情", description = "根据题目id查询可编辑的题目详情")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "查询成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<ProblemEditVO> get(
            @Parameter(name = "id", description = "题目id", required = true, in = ParameterIn.PATH)
            @PathVariable @NotNull(message = "题目id不能为空") Integer id) {
        return Result.success(problemService.getProblem(id, ProblemEditVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询题目", description = "按题目名称分页查询题目")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "查询成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<PageVO<ProblemPageVO>> page(@ParameterObject @Valid GeneralPageQueryDTO queryDTO) {
        return Result.success(problemService.pageQuery(queryDTO));
    }

    @PutMapping("/tag")
    @Operation(summary = "设置题目标签", description = "覆盖题目原有标签集合")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "设置成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<Void> setTags(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "题目标签设置参数", required = true)
            @RequestBody @Valid SetTagsDTO setTagsDTO) {
        problemService.setTags(setTagsDTO);
        return Result.success();
    }

    @GetMapping("/export")
    @Operation(summary = "导出题目", description = "导出题目 Excel，包含逗号分隔的标签名称")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "导出成功",
            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    schema = @Schema(type = "string", format = "binary"))))
    public ResponseEntity<byte[]> exportProblems() {
        return excelResponse("题目导出.xlsx", problemService.exportProblems());
    }

    @GetMapping("/import/template")
    @Operation(summary = "下载题目导入模板", description = "下载题目 Excel 导入模板")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "下载成功",
            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    schema = @Schema(type = "string", format = "binary"))))
    public ResponseEntity<byte[]> exportProblemTemplate() {
        return excelResponse("题目导入模板.xlsx", problemService.exportProblemTemplate());
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "导入题目", description = "导入题目 Excel，id 或标题已存在时跳过并报告")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "导入完成",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<ImportResultVO> importProblems(
            @Parameter(name = "file", description = "题目 Excel 文件，仅支持 .xlsx", required = true)
            @RequestParam("file") MultipartFile file) {
        return Result.success(problemService.importProblems(file));
    }

    private ResponseEntity<byte[]> excelResponse(String fileName, byte[] data) {
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                .body(data);
    }
}
