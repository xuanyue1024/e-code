package com.ecode.entity.po;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
public class CodeSubmission implements Serializable {

    @Serial
    private static final long serialVersionUID = -4656789173164441260L;

    @Schema(description = "代码内容")
    String codeText;

    @Schema(description = "提交时间")
    LocalDateTime submitTime;

    @Schema(description = "编程语言类型")
    String languageType;

    @Schema(description = "通过的测试用例数")
    Integer passCount;
}