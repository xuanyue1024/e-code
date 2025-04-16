package com.ecode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description="ProblemUpdateDTO对象")
public class ProblemUpdateDTO extends ProblemAddDTO{
    private static final long serialVersionUID = -5419034326902870843L;

    @Schema(description = "题目id")
    private Integer id;

}
