package com.ecode.service;

import com.ecode.dto.DebugCodeDTO;
import com.ecode.dto.RunCodeDTO;
import com.ecode.entity.po.CodeSubmission;
import com.ecode.vo.RunCodeVO;

import java.util.List;


public interface CodeService {
    /**
     * 调试代码
     *
     * @param debugCodeDTO 调试代码dto
     * @return 字符串
     */
    String debugCode(DebugCodeDTO debugCodeDTO);

    /**
     * 运行代码(测试题目）
     *
     * @param runCodeDTO 运行代码dto
     * @return 字符串
     */
    RunCodeVO runCode(RunCodeDTO runCodeDTO);

    /**
     * 获取代码提交记录
     *
     * @return 代码提交记录列表
     */
    List<CodeSubmission> getCodeSubmissions(Integer studentId, Integer classId, Integer classProblemId);
}
