package com.ecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 导入汇总结果。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
@Data
@Schema(description = "Excel 导入汇总结果")
public class ImportResultVO implements Serializable {

    private static final long serialVersionUID = 2037280419523588722L;

    @Schema(description = "总数据行数，不含表头")
    private int total;

    @Schema(description = "新增行数")
    private int created;

    @Schema(description = "跳过行数")
    private int skipped;

    @Schema(description = "失败行数")
    private int failed;

    @Schema(description = "逐行处理明细")
    private List<ImportRowResultVO> rows = new ArrayList<>();

    public void addCreated(int rowNumber, String message) {
        created++;
        rows.add(ImportRowResultVO.builder().rowNumber(rowNumber).status("CREATED").message(message).build());
    }

    public void addSkipped(int rowNumber, String message) {
        skipped++;
        rows.add(ImportRowResultVO.builder().rowNumber(rowNumber).status("SKIPPED").message(message).build());
    }

    public void addFailed(int rowNumber, String message) {
        failed++;
        rows.add(ImportRowResultVO.builder().rowNumber(rowNumber).status("FAILED").message(message).build());
    }
}
