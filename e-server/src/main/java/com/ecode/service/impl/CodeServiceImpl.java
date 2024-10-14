package com.ecode.service.impl;

import com.ecode.dto.DebugCodeDTO;
import com.ecode.properties.DockerProperties;
import com.ecode.service.CodeService;
import com.ecode.utils.RunCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeServiceImpl implements CodeService {

    @Autowired
    private DockerProperties dockerProperties;

    @Override
    public String debugCode(DebugCodeDTO debugCodeDTO) {
        return RunCodeUtil.runCode(dockerProperties.getUrl(),
                dockerProperties.getTimeout(), debugCodeDTO.getType(),
                debugCodeDTO.getCode(), debugCodeDTO.getInput());
    }
}
