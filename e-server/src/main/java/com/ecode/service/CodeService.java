package com.ecode.service;

import com.ecode.dto.DebugCodeDTO;


public interface CodeService {
    /**
     * 调试代码
     *
     * @param debugCodeDTO 调试代码dto
     * @return 字符串
     */
    String debugCode(DebugCodeDTO debugCodeDTO);
}
