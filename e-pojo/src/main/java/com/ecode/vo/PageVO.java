package com.ecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Schema(description = "分页结果")
@NoArgsConstructor
@AllArgsConstructor
public class PageVO<T> {
    @Schema(description = "总条数")
    private Long total;
    @Schema(description = "总页数")
    private Long pages;
    @Schema(description = "集合")
    private List<T> records;
}