package com.ecode.utils;

import com.ecode.constant.MessageConstant;
import com.ecode.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileUtil {

    private final S3Util s3Util;

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
            String objectName = UUID.randomUUID() + extension;

            //文件的请求路径
            if (s3Util.upload(file.getInputStream(), file.getSize(), objectName)){
                return objectName;
            }
            throw new BaseException("s3 上传失败");
        } catch (IOException e) {
            log.error("文件上传失败：{}", e.getMessage());
            throw new BaseException(MessageConstant.FILE_UPLOAD_FAIL + "," + e.getMessage());
        }
    }

    public void deleteFileByUrl(String objectName){
        s3Util.delete(objectName);
    }

}
