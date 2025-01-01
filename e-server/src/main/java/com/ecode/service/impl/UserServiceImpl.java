package com.ecode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.dto.UserRegisterDTO;
import com.ecode.dto.UserUpdateDTO;
import com.ecode.entity.User;
import com.ecode.enumeration.UserStatus;
import com.ecode.exception.RegisterException;
import com.ecode.mapper.UserMapper;
import com.ecode.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  用户service实现类
 * </p>
 *
 * @author 竹林听雨
 * @since 2024-09-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void save(UserRegisterDTO userRegisterDTO) {
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps("email:captcha:" + userRegisterDTO.getEmail());
        String captcha = hashOps.get("captcha");
        if (captcha == null || userRegisterDTO.getEmailCode() == null){
            throw new RegisterException(MessageConstant.REGISTRATION_FAILED);
        }
        if (!userRegisterDTO.getEmailCode().equals(captcha)){
            throw new RegisterException(MessageConstant.REGISTRATION_FAILED_CAPTCHA);
        }
        redisTemplate.delete("email:captcha:" + userRegisterDTO.getEmail());
        User user = User.builder()
                .status(UserStatus.ENABLE)
                .name("默认用户")
                .profilePicture("https://web-tlias-itheima1024.oss-cn-chengdu.aliyuncs.com/23f57464-eb9b-42a6-b98e-91ab1937fbf3.jpg")
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .score(0L)
                .build();

        BeanUtils.copyProperties(userRegisterDTO, user);
        userMapper.insert(user);

    }

    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO, Integer id) {
        User u = new User();
        BeanUtils.copyProperties(userUpdateDTO, u);
        u.setId(BaseContext.getCurrentId());

        userMapper.updateById(u);
    }

    @Override
    public User getUserInfo(Integer currentId) {
        return userMapper.selectById(currentId);
    }
}
