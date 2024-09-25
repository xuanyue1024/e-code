package com.ecode.service.impl;

import com.ecode.constant.MessageConstant;
import com.ecode.enumeration.EmailTemplateEnum;
import com.ecode.exception.MailCaptchaException;
import com.ecode.service.CaptchaService;
import com.ecode.utils.EmailApi;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private EmailApi emailApi;

    @Override
    public void sendCaptcha(String email) {
        sendMailCaptcha(email);
    }

    /**
     * 发送邮件验证码方法
     *
     * @param email 客户邮箱
     * @return boolean
     */
    private boolean sendMailCaptcha(String email){
        String key = "email:captcha:"+email;
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
        // 初始检查
        String lastSendTimestamp = hashOps.get("lastSendTimestamp");
        String sendCount = hashOps.get("sendCount");

        if(StringUtils.isNotBlank(sendCount)&&Integer.parseInt(sendCount)>=5){
            hashOps.expire(24, TimeUnit.HOURS);
            throw new MailCaptchaException(MessageConstant.CAPTCHA_TRY_AGAIN_WITHIN24_HOURS);
        }
        if(StringUtils.isNotBlank(lastSendTimestamp)){
            long lastSendTime = Long.parseLong(lastSendTimestamp);
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - lastSendTime;
            if(elapsedTime < 60 * 1000){
                throw new MailCaptchaException(MessageConstant.CAPTCHA_TRY_AGAIN_WITHIN60_S);
            }
        }
        int newSendCount = StringUtils.isNotBlank(sendCount) ? Integer.parseInt(sendCount) + 1 : 1;
        String captcha = RandomStringUtils.randomNumeric(6);
        EmailTemplateEnum emailTemplateEnum = EmailTemplateEnum.VERIFICATION_CODE_EMAIL_HTML;

        try {
            emailApi.sendHtmlEmail(emailTemplateEnum.getSubject(), emailTemplateEnum.set(captcha), email);
        } catch (Exception e) {
            throw new MailCaptchaException(MessageConstant.CAPTCHA_ERROR);
        }
        hashOps.put("captcha", captcha);
        hashOps.put("lastSendTimestamp", String.valueOf(System.currentTimeMillis()));
        hashOps.put("sendCount", String.valueOf(newSendCount));
        hashOps.expire(15, TimeUnit.MINUTES); // 设置过期时间为15分钟

        return true;
    }
}
