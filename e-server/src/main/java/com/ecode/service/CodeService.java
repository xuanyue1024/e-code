package com.ecode.service;

import com.ecode.dto.DebugCodeDTO;
import com.ecode.dto.RunCodeDTO;
import com.ecode.vo.RunCodeVO;


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

}
