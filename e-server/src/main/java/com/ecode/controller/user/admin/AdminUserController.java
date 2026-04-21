package com.ecode.controller.user.admin;

import com.ecode.dto.AdminUserCreateDTO;
import com.ecode.dto.AdminUserPageQueryDTO;
import com.ecode.dto.AdminUserStatusDTO;
import com.ecode.dto.AdminTokenLogoutDTO;
import com.ecode.dto.AdminUserUpdateDTO;
import com.ecode.result.Result;
import com.ecode.service.AdminSessionService;
import com.ecode.service.UserService;
import com.ecode.vo.AdminUserVO;
import com.ecode.vo.ImportResultVO;
import com.ecode.vo.PageVO;
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
import org.springframework.web.bind.annotation.PatchMapping;
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
 * 管理员用户管理接口。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "管理员-用户管理", description = "管理员用户查询、创建、更新、启禁用与删除接口")
@RequestMapping("/admin/user")
public class AdminUserController {

    private final UserService userService;
    private final AdminSessionService adminSessionService;

    @GetMapping("/page")
    @Operation(summary = "分页查询用户", description = "按名称、用户名、邮箱、角色和状态分页查询用户，响应不包含密码")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功",
                    content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "403", description = "无管理员权限", content = @Content)
    })
    public Result<PageVO<AdminUserVO>> page(@ParameterObject @Valid AdminUserPageQueryDTO queryDTO) {
        return Result.success(userService.adminPage(queryDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询用户详情", description = "根据用户id查询用户详情，响应不包含密码")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功",
                    content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "403", description = "无管理员权限", content = @Content)
    })
    public Result<AdminUserVO> get(
            @Parameter(name = "id", description = "用户id", required = true, in = ParameterIn.PATH)
            @PathVariable @NotNull(message = "用户id不能为空") Integer id) {
        return Result.success(userService.adminGetById(id));
    }

    @PostMapping
    @Operation(summary = "创建用户", description = "管理员创建用户，可指定角色和账号状态")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "创建成功",
                    content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "403", description = "无管理员权限", content = @Content)
    })
    public Result<Void> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "用户创建参数", required = true)
            @RequestBody @Valid AdminUserCreateDTO createDTO) {
        userService.adminCreate(createDTO);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "更新用户", description = "管理员更新用户资料、角色和状态；密码为空时不修改密码")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "更新成功",
                    content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "403", description = "无管理员权限", content = @Content)
    })
    public Result<Void> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "用户更新参数", required = true)
            @RequestBody @Valid AdminUserUpdateDTO updateDTO) {
        userService.adminUpdate(updateDTO);
        return Result.success();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "修改用户状态", description = "启用或禁用指定用户")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "修改成功",
                    content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "403", description = "无管理员权限", content = @Content)
    })
    public Result<Void> updateStatus(
            @Parameter(name = "id", description = "用户id", required = true, in = ParameterIn.PATH)
            @PathVariable @NotNull(message = "用户id不能为空") Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "用户状态参数", required = true)
            @RequestBody @Valid AdminUserStatusDTO statusDTO) {
        userService.adminUpdateStatus(id, statusDTO.getStatus());
        return Result.success();
    }

    @DeleteMapping
    @Operation(summary = "批量删除用户", description = "根据用户id集合批量删除用户")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "删除成功",
                    content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "403", description = "无管理员权限", content = @Content)
    })
    public Result<Void> deleteBatch(
            @Parameter(name = "ids", description = "用户id集合", required = true, in = ParameterIn.QUERY)
            @RequestParam @NotEmpty(message = "用户id集合不能为空") List<Integer> ids) {
        userService.adminDeleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/{id}/logout")
    @Operation(summary = "下线用户", description = "让指定用户已签发的全部 token 失效，用户重新登录后可继续使用")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "下线成功",
                    content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "403", description = "无管理员权限", content = @Content)
    })
    public Result<Void> logoutUser(
            @Parameter(name = "id", description = "用户id", required = true, in = ParameterIn.PATH)
            @PathVariable @NotNull(message = "用户id不能为空") Integer id) {
        adminSessionService.logoutUser(id);
        return Result.success();
    }

    @PostMapping("/logout/token")
    @Operation(summary = "下线指定 token", description = "让指定 JWT token 失效，不影响同账号其他 token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "下线成功",
                    content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "403", description = "无管理员权限", content = @Content)
    })
    public Result<Void> logoutToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "token 下线参数", required = true)
            @RequestBody @Valid AdminTokenLogoutDTO tokenLogoutDTO) {
        adminSessionService.logoutToken(tokenLogoutDTO.getToken());
        return Result.success();
    }

    @GetMapping("/export")
    @Operation(summary = "导出用户", description = "导出用户 Excel，密码列为空")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "导出成功",
            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    schema = @Schema(type = "string", format = "binary"))))
    public ResponseEntity<byte[]> exportUsers() {
        return excelResponse("用户导出.xlsx", userService.exportUsers());
    }

    @GetMapping("/import/template")
    @Operation(summary = "下载用户导入模板", description = "下载用户 Excel 导入模板")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "下载成功",
            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    schema = @Schema(type = "string", format = "binary"))))
    public ResponseEntity<byte[]> exportUserTemplate() {
        return excelResponse("用户导入模板.xlsx", userService.exportUserTemplate());
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "导入用户", description = "导入用户 Excel，已存在的 username 或 email 会跳过并报告")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "导入完成",
            content = @Content(schema = @Schema(implementation = Result.class))))
    public Result<ImportResultVO> importUsers(
            @Parameter(name = "file", description = "用户 Excel 文件，仅支持 .xlsx", required = true)
            @RequestParam("file") MultipartFile file) {
        return Result.success(userService.importUsers(file));
    }

    private ResponseEntity<byte[]> excelResponse(String fileName, byte[] data) {
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                .body(data);
    }
}
