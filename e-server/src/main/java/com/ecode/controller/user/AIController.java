package com.ecode.controller.user;

import com.ecode.constant.JwtClaimsConstant;
import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.dto.AiInputDTO;
import com.ecode.exception.SSEException;
import com.ecode.properties.JwtProperties;
import com.ecode.result.Result;
import com.ecode.service.AIService;
import com.ecode.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
@Tag(name = "AI管理")
@RequestMapping("/user/ai")
//@CrossOrigin(origins = "*")  // 允许所有来源的跨域请求
public class AIController {

    @Autowired
    private AIService aiService;
    @Autowired
    private JwtProperties  jwtProperties;

    /**
     * 流式输出聊天内容的接口
     *
     * @param aiInputDTO Ai输入
     * @return Flux<Result < String>>返回流式聊天内容
     */
    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "聊天")
    public Flux<Result<String>> chat(@RequestBody AiInputDTO aiInputDTO) {
        jwtVerify(aiInputDTO.getToken());
        return aiService.getChat(aiInputDTO);
    }

    private void jwtVerify(String token){
        try {
            log.info("jwt-ai访问校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Integer empId = Integer.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("当前用户id：{}", empId);
            BaseContext.setCurrentId(empId);
            //通过，放行
        } catch (Exception ex) {
            //不通过，抛出异常
            throw new SSEException(MessageConstant.TOKEN_FAILURE);
        }
    }

}
