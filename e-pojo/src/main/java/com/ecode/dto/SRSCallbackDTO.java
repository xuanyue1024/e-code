package com.ecode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SRSCallbackDTO {

    @Schema(description = "事件行为,推流-on_publish,拉流-on_play")
    private String action;

    @Schema(description = "客户端ID")
    private String clientId;

    @Schema(description = "IP地址")
    private String ip;

    private String vhost;

    @Schema(description = "app名称")
    private String app;

    @Schema(description = "流名称")
    private String stream;

    @Schema(description = "携带字段")
    private String param;

    private String serverId;
    private String streamUrl;
    private String streamId;

}