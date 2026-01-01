package com.ecode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 竹林听雨
 * @since 2025-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@TableName("file")
@Schema(description="File对象")
@NoArgsConstructor
@AllArgsConstructor
public class File implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "文件id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "S3对象名称")
    @TableField("object_name")
    private String objectName;

    @Schema(description = "SHA-256十六进制字符串(固定 64 字符）")
    @TableField("content_hash")
    private String contentHash;

    @Schema(description = "文件字节数")
    @TableField("size_bytes")
    private Long sizeBytes;

    @Schema(description = "原始文件名称")
    @TableField("original_filename")
    private String originalFilename;

    @Schema(description = "MIME 类型（如 image/jpeg, application/pdf）")
    @TableField("mime_type")
    private String mimeType;

    @Schema(description = "引用计数")
    @TableField("ref_count")
    private Integer refCount;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "删除标记（暂不启用）")
    @TableField("deleted_at")
    private LocalDateTime deletedAt;


}
