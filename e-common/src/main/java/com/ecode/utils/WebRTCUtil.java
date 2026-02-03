package com.ecode.utils;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * WebRTC HTTP 工具类,目前适用于 SRS
 *
 * @author 竹林听雨
 * @version 1.0
 * @since 2026-02-01  17:38
 */
@Data
@Builder
public class WebRTCUtil {

    private String baseUrl;

    private String appName;

    @AllArgsConstructor
    public static enum Status {
        WHIP("whip"),//推流
        WHEP("whep");//拉流

        private final String name;
    }

    public String start(Status status, String appName, String streamKey, String sdpOffer) {

        String url = baseUrl + "/" + status.name + "/?app=" + appName + "&stream=" + streamKey;

        return OkHttpUtils.builder()
                .url(url)
                .postRaw(sdpOffer, "application/sdp")
                .async();
    }

    public String start(Status status, String streamKey, String sdpOffer) {
        return this.start(status, appName, streamKey, sdpOffer);
    }
}
