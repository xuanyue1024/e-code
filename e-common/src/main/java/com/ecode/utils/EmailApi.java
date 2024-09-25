package com.ecode.utils;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Objects;
 
/**
 * @author mijiupro
 */
@Component
@Slf4j
public class EmailApi {
    @Autowired
    private JavaMailSender mailSender;
 
    @Value("${spring.mail.username}")
    private String from ;// 发件人

    /**
     * 发送纯文本的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @return 是否成功
     */
    @SneakyThrows(Exception.class)
    public boolean sendGeneralEmail(String subject, String content, String... to){
        // 创建邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        // 设置收件人
        message.setTo(to);
        // 设置邮件主题
        message.setSubject(subject);
        // 设置邮件内容
        message.setText(content);
 
        // 发送邮件
        mailSender.send(message);
 
        return true;
    }
    /**
     * 发送html的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @return 是否成功
     */
    @SneakyThrows(Exception.class)
    public boolean sendHtmlEmail(String subject, String content, String... to){
        // 创建邮件消息
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        // 设置收件人
        helper.setTo(to);
        // 设置邮件主题
        helper.setSubject(subject);
        // 设置邮件内容
        helper.setText(content, true);
 
        // 发送邮件
        mailSender.send(mimeMessage);
 
        log.info("发送邮件成功");
        return true;
 
    }
    /**
     * 发送带附件的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param filePaths 附件路径
     * @return 是否成功
     */
    @SneakyThrows(Exception.class)
    public boolean sendAttachmentsEmail(String subject, String content, String[] to, String[] filePaths) {
        // 创建邮件消息
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        // 设置收件人
        helper.setTo(to);
        // 设置邮件主题
        helper.setSubject(subject);
        // 设置邮件内容
        helper.setText(content,true);
 
        // 添加附件
        if (filePaths != null) {
            for (String filePath : filePaths) {
                FileSystemResource file = new FileSystemResource(new File(filePath));
                helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
 
            }
        }
        // 发送邮件
        mailSender.send(mimeMessage);
        return true;
    }
 
    /**
     * 发送带静态资源的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param rscPath 静态资源路径
     * @param rscId 静态资源id
     * @return 是否成功
     */
    @SneakyThrows(Exception.class)
    public boolean sendInlineResourceEmail(String subject, String content, String to, String rscPath, String rscId) {
        // 创建邮件消息
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        // 设置发件人
        helper.setFrom(from);
        // 设置收件人
        helper.setTo(to);
        // 设置邮件主题
        helper.setSubject(subject);
 
        //html内容图片
        String contentHtml = "<html><body>这是邮件的内容，包含一个图片：<img src=\'cid:" + rscId + "\'>"+content+"</body></html>";
 
        helper.setText(contentHtml, true);
        //指定讲资源地址
        FileSystemResource res = new FileSystemResource(new File(rscPath));
        helper.addInline(rscId, res);
 
        mailSender.send(mimeMessage);
        return true;
    }
 
}