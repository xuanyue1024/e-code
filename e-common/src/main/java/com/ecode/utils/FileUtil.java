package com.ecode.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class FileUtil {
    @Autowired
    private AliOssUtil aliOssUtil;


    /**
     * 上传文件到服务器
     *
     * @param file 需要上传的文件
     * @return 文件在服务器上的访问路径，上传失败时返回null
     */
    public String uploadFile(MultipartFile file){
        log.info("文件上传：{}",file);

        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀   dfdfdf.png
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称
            String objectName = UUID.randomUUID().toString() + extension;

            //文件的请求路径
            return aliOssUtil.upload(file.getBytes(), objectName);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }
        return null;
    }
    public void deleteFileByUrl(String url){
        int lastSlashIndex = url.lastIndexOf('/');
        String fileName = url.substring(lastSlashIndex + 1);
        aliOssUtil.delete(fileName);
    }

}
