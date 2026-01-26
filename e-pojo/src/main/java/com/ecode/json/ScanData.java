package com.ecode.json;

import com.ecode.enumeration.ScanStatus;
import lombok.*;

/**
 * Redis存储-扫码数据
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScanData {
    private ScanStatus status;
    private Integer userId;
}
