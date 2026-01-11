package com.ecode.config;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.resource.CrudResourceStore;
import cloud.tianai.captcha.resource.ResourceStore;
import cloud.tianai.captcha.resource.common.model.dto.Resource;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@RequiredArgsConstructor
public class CaptchaResourceConfiguration {

    private final ResourceStore resourceStore;

    @PostConstruct
    public void init() {
        CrudResourceStore resourceStore = (CrudResourceStore) this.resourceStore;
        // 2. 添加自定义背景图片
       resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha/a.jpg", "default"));
       resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha/c.jpg", "default"));
       resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha/d.jpg", "default"));
       resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha/g.jpg", "default"));
       resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha/h.jpg", "default"));
       resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha/i.jpg", "default"));
       resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha/j.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha/48.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha/y1.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha/y2.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha/y3.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha/y4.jpg", "default"));

       resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", "captcha/48.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", "captcha/y1.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", "captcha/y2.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", "captcha/y3.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", "captcha/y4.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", "captcha/g.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", "captcha/i.jpg", "default"));

       resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource("classpath", "captcha/48.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource("classpath", "captcha/y1.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource("classpath", "captcha/y2.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource("classpath", "captcha/y3.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource("classpath", "captcha/y4.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource("classpath", "captcha/a.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource("classpath", "captcha/h.jpg", "default"));

       resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource("classpath", "captcha/c.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource("classpath", "captcha/h.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource("classpath", "captcha/a.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource("classpath", "captcha/y3.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource("classpath", "captcha/y4.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource("classpath", "captcha/h.jpg", "default"));
    }
}
