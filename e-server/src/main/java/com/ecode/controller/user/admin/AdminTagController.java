package com.ecode.controller.user.admin;

import com.ecode.result.Result;
import com.ecode.service.TagService;
import com.ecode.vo.TagVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员标签管理接口。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "管理员-标签管理", description = "管理员题目标签增删查接口")
@RequestMapping("/admin/tag")
public class AdminTagController {

    private final TagService tagService;

    @PostMapping("/{name}")
    @Operation(summary = "新增标签", description = "新增单个标签并返回标签id")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "新增成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<Integer> add(
            @Parameter(name = "name", description = "标签名称", required = true, in = ParameterIn.PATH)
            @PathVariable @NotBlank(message = "标签名称不能为空") String name) {
        com.ecode.entity.Tag tag = new com.ecode.entity.Tag(null, name, LocalDateTime.now());
        tagService.save(tag);
        return Result.success(tag.getId());
    }

    @PostMapping("/batch")
    @Operation(summary = "批量新增标签", description = "按标签名称集合批量新增标签，已存在的标签直接返回已有id")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "新增成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<List<Integer>> addBatch(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "标签名称集合", required = true)
            @RequestBody @NotEmpty(message = "标签名称集合不能为空") List<String> names) {
        return Result.success(tagService.addTags(names));
    }

    @DeleteMapping
    @Operation(summary = "批量删除标签", description = "根据标签id集合批量删除标签")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "删除成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<Void> deleteBatch(
            @Parameter(name = "ids", description = "标签id集合", required = true, in = ParameterIn.QUERY)
            @RequestParam @NotEmpty(message = "标签id集合不能为空") List<Integer> ids) {
        tagService.removeBatchByIds(ids);
        return Result.success();
    }

    @GetMapping
    @Operation(summary = "模糊查询标签", description = "按标签名称模糊查询，不传名称时返回全部标签")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "查询成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<List<TagVO>> getByName(
            @Parameter(name = "name", description = "标签名称关键字", in = ParameterIn.QUERY)
            @RequestParam(required = false) String name) {
        return Result.success(tagService.getByName(name));
    }

    @GetMapping("/getByIds")
    @Operation(summary = "按id集合查询标签", description = "根据标签id集合返回标签信息")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "查询成功",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<List<TagVO>> getByIds(
            @Parameter(name = "ids", description = "标签id集合", required = true, in = ParameterIn.QUERY)
            @RequestParam @NotEmpty(message = "标签id集合不能为空") List<Integer> ids) {
        return Result.success(tagService.getByIds(ids));
    }
}
