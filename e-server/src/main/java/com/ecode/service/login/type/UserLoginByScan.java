package com.ecode.service.login.type;

import com.ecode.constant.MessageConstant;
import com.ecode.dto.UserLoginDTO;
import com.ecode.entity.User;
import com.ecode.enumeration.Redis;
import com.ecode.enumeration.ScanStatus;
import com.ecode.exception.LoginException;
import com.ecode.json.ScanData;
import com.ecode.mapper.UserMapper;
import com.ecode.service.login.LoginStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 扫码登录
 *
 * @author 竹林听雨
 * @since  2026/01/26
 */
@Service("scan")
@RequiredArgsConstructor
public class UserLoginByScan implements LoginStrategy {

    private final UserMapper userMapper;

    private final RedisTemplate redisTemplate;

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String sceneId = userLoginDTO.getSceneId();
        ScanData scanData = (ScanData) redisTemplate.opsForValue().get(Redis.LOGIN_SCAN + sceneId);
        if (scanData == null || scanData.getUserId() == null) {
            throw new LoginException(MessageConstant.SCAN_EXPIRED);
        }
        if (scanData.getStatus() != ScanStatus.CONFIRMED){
            throw new LoginException(MessageConstant.SCAN_STATUS_ERROR);
        }

        User user = userMapper.selectById(scanData.getUserId());

        if (user == null){
            throw new LoginException(MessageConstant.LOGIN_FAILED);
        }
        return user;
    }
}
