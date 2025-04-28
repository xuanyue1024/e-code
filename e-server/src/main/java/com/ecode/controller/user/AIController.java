package com.ecode.controller.user;

import com.ecode.context.BaseContext;
import com.ecode.dto.AiInputDTO;
import com.ecode.enumeration.AiType;
import com.ecode.result.Result;
import com.ecode.service.AIService;
import com.ecode.service.AiChatHistoryService;
import com.ecode.vo.AiChatIdsVO;
import com.ecode.vo.AiMessageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@Tag(name = "AI管理")
@RequestMapping("/user/ai")
//@CrossOrigin(origins = "*")  // 允许所有来源的跨域请求
public class AIController {

    @Autowired
    private AIService aiService;
    @Autowired
    private AiChatHistoryService aiChatHistoryService;
    @Autowired
    private ChatMemory chatMemory;

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

    @PostMapping(value = "/questionAnswer", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "题目解答")
    public Flux<Result<String>> questionAnswer(@RequestBody @Valid AiInputDTO aiInputDTO) {
        return aiService.questionAnswer(aiInputDTO);
    }

    /**
     * 生成题目的接口
     *
     * @param require 题目要求
     * @return Flux<Result < String>>返回生成的题目
     */
    @GetMapping(value = "/generate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "生成题目")
    public Flux<Result<String>> generate(@NotBlank String require) {
        return aiService.generateQuestion(require);
    }

    /**
     * 创建聊天ID。
     *
     * @param type 指定创建聊天ID的类型，必须是"pdf"或"chat"
     * @return 返回创建聊天ID的结果，包含字符串类型的聊天ID
     */
    @GetMapping("/creat")
    @Operation(summary = "创建聊天ID")
    public Result<String> createChatId(@RequestParam @NotNull AiType type){
        String chatId = aiChatHistoryService.createChatId(BaseContext.getCurrentId(), type);
        return Result.success(chatId);
    }


    /**
     * 查询会话历史列表
     *
     * @param type 业务类型
     * @return chatId列表
     */
    @GetMapping("/history/{type}")
    @Operation(summary = "查询会话id列表")
    public Result<List<AiChatIdsVO>> getChatIds(@PathVariable("type") AiType type) {
        List<AiChatIdsVO> chatIds = aiChatHistoryService.getChatIds(BaseContext.getCurrentId(), type);
        return Result.success(chatIds);
    }

    /**
     * 根据业务类型、chatId查询会话历史
     * @param type 业务类型
     * @param chatId 会话id
     * @return 指定会话的历史消息
     */
    @GetMapping("/history/{type}/{chatId}")
    @Operation(summary = "查询单个会话历史")
    public Result<List<AiMessageVO>> getChatHistory(@PathVariable("type") AiType type, @PathVariable("chatId") String chatId) {
        List<Message> messages = chatMemory.get(chatId, Integer.MAX_VALUE);
        if(messages == null) {
            return Result.success(List.of());
        }
        List<AiMessageVO> messageVOS = messages.stream().map(AiMessageVO::new).toList();
        return Result.success(messageVOS);
    }

    //删除会话
    @DeleteMapping("/history/{type}/{chatId}")
    @Operation(summary = "删除会话")
    public Result deleteChatHistory(@PathVariable("type") AiType type, @PathVariable("chatId") String chatId) {
        aiChatHistoryService.deleteChatId(chatId, BaseContext.getCurrentId());
        chatMemory.clear(chatId);
        return Result.success();
    }
}
