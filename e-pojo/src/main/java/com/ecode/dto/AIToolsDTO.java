package com.ecode.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AIToolsDTO implements Serializable {

    private Integer userId;

    private String userName;

    private Integer classId;

    private String className;

    private Integer tagId;

    private String tagName;

    private Integer problemId;

    private String problemTitle;

    private Integer problemGrade;
}
