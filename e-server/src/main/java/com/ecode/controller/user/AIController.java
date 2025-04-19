package com.ecode.controller.user;

import com.ecode.context.BaseContext;
import com.ecode.dto.AiInputDTO;
import com.ecode.result.Result;
import com.ecode.service.AIService;
import com.ecode.service.AiChatHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@Tag(name = "AI管理")
@RequestMapping("/user/ai")
//@CrossOrigin(origins = "*")  // 允许所有来源的跨域请求
public class AIController {

    @Autowired
    private AIService aiService;
    @Autowired
    private AiChatHistoryService aiChatHistoryService;

    /**
     * 流式输出聊天内容的接口
     *
     * @param aiInputDTO Ai输入
     * @return Flux<Result < String>>返回流式聊天内容
     */
    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "聊天")
    public Flux<Result<String>> chat(@RequestBody @Valid AiInputDTO aiInputDTO) {
        return aiService.getChat(aiInputDTO);
    }

    /**
     * 创建聊天ID。
     *
     * @param type 指定创建聊天ID的类型，必须是"pdf"或"chat"
     * @return 返回创建聊天ID的结果，包含字符串类型的聊天ID
     */
    @GetMapping("/creat")
    public Result<String> createChatId(@RequestParam @NotBlank @Pattern(regexp = "pdf|chat") String type){
        String chatId = aiChatHistoryService.createChatId(BaseContext.getCurrentId(), type);
        return Result.success(chatId);
    }

}
