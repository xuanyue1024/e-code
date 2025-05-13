package com.ecode.entity.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Web文件操作类：提供文件的基础操作和构建功能
 *
 * @author 竹林听雨
 * @date 2025/05/09
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Schema(description = "知识库文件,data为空表示没有文件")
public class RepositoryFile {
    // 文件名称
    @Schema(description = "文件名称")
    private String name;
    @Schema(description = "文件链接")
    private String url;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
