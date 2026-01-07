package com.ecode.utils;

import java.util.UUID;

/**
 * 一些杂项工具类
 */
public class EUtil {
    /**
     * MYSQL 判断是否唯一索引冲突
     * @param e 异常
     * @return
     */
    public static boolean isDuplicateKeyException(Exception e) {
        // MySQL: Duplicate entry 'xxx' for key 'uk_hash_size'
        return e.getMessage() != null &&
                (e.getMessage().contains("Duplicate entry"));
    }

    /**
     * 获取文件扩展名
     * @param filename
     * @return
     */
    public static String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) return "";
        return filename.substring(filename.lastIndexOf('.')).toLowerCase();
    }

    /**
     * 校验是否符合uuid标准
     * @param str uuid字符串
     * @return 是否满足
     */
    public static boolean isUUID(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            UUID.fromString(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否为合法的无连字符 UUID（32 位十六进制）
     */
    public static boolean isAssignUUID(String str) {
        if (str == null || str.length() != 32) {
            return false;
        }
        // 使用正则匹配 32 个十六进制字符（不区分大小写）
        return str.matches("[0-9a-fA-F]{32}");
    }
}
