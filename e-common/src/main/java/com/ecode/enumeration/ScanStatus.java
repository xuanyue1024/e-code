package com.ecode.enumeration;

/**
 * 扫码登录状态枚举
 */
public enum ScanStatus {
    WAITING,        // 等待扫码
    SCANNED,        // 已扫码，等待确认
    CONFIRMED,      // 已确认登录
    EXPIRED,        // 二维码过期
    CANCELLED       // 用户取消
}