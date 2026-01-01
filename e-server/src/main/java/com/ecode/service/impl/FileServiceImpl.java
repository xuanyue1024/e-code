package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecode.constant.MessageConstant;
import com.ecode.entity.File;
import com.ecode.exception.BaseException;
import com.ecode.mapper.FileMapper;
import com.ecode.service.FileService;
import com.ecode.utils.EUtil;
import com.ecode.utils.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 *  File 服务实现类
 * </p>
 *
 * @author 竹林听雨
 * @since 2025-12-27
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    private final S3Util s3Util;

    private final FileMapper fileMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadFile(MultipartFile file, String path)  {
        log.info("文件上传：{}",file);

        //先计算一遍hash值
        String hash;
        try(InputStream is = file.getInputStream()){
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

            hash = HexFormat.of().formatHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error("环境错误：不支持SHA-256算法", e);
            throw new BaseException("服务器内部配置错误");
        } catch (IOException e) {
            log.error("读取上传文件流失败", e);
            throw new BaseException("文件读取失败，请稍后重试");
        }

        long size = file.getSize();

        int i = fileMapper.update(new LambdaUpdateWrapper<File>()
                .eq(File::getContentHash, hash)
                .eq(File::getSizeBytes, size)
                .gt(File::getRefCount, 0)
                .setSql("ref_count = ref_count + 1"));

        if (i > 0){
            return fileMapper.selectOne(new LambdaQueryWrapper<File>()
                    .select(File::getObjectName)
                    .eq(File::getContentHash, hash)
                    .eq(File::getSizeBytes, size)).getObjectName();
        }

        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //截取原始文件名的后缀   dfdfdf.png
        String extension = EUtil.getFileExtension(originalFilename);
        //构造新文件名称
        path = (path == null || path.isEmpty()) ? "" : path + "/";
        String objectName = path + UUID.randomUUID().toString().replace("-", "") + extension;

        try(InputStream uploadStream = file.getInputStream()){
            if (!s3Util.upload(uploadStream, size, file.getContentType(), objectName)){
                throw new BaseException(MessageConstant.FILE_UPLOAD_FAIL);
            }
        }catch (IOException e){
            log.error("S3上传时读取文件流异常: {}", objectName, e);
            throw new BaseException("上传对象存储失败");
        }

        fileMapper.insert(File.builder()
                .contentHash(hash)
                .sizeBytes(size)
                .mimeType(file.getContentType())
                .originalFilename(originalFilename)
                .objectName(objectName)
                .refCount(1)
                .createTime(LocalDateTime.now())
                .build());

        return objectName;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFileByObjectName(String objectName) {

        File file = fileMapper.selectOne(new LambdaQueryWrapper<File>()
                .eq(File::getObjectName, objectName));
        if (file == null) return;

        fileMapper.update(new LambdaUpdateWrapper<File>()
                .eq(File::getId, file.getId())
                .gt(File::getRefCount, 0)
                .setSql("ref_count = ref_count - 1"));

        File fileById = fileMapper.selectById(file.getId());
        if (fileById != null && fileById.getRefCount() <= 0) {
            fileMapper.deleteById(file.getId());//此代码注释保留数据库记录(ref_count = 0)

            //异步删除s3,避免外部调用导致回滚或者拖慢事务
            CompletableFuture.runAsync(() -> {
                s3Util.delete(objectName);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadFile(MultipartFile file) {
        return uploadFile(file, null);
    }
}
