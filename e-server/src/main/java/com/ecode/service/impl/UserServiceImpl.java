package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.dto.UserRegisterDTO;
import com.ecode.dto.UserUpdateDTO;
import com.ecode.entity.OauthIdentities;
import com.ecode.entity.User;
import com.ecode.enumeration.Redis;
import com.ecode.enumeration.ScanStatus;
import com.ecode.enumeration.UserStatus;
import com.ecode.exception.BaseException;
import com.ecode.exception.RegisterException;
import com.ecode.json.ScanData;
import com.ecode.mapper.UserMapper;
import com.ecode.service.OauthIdentitiesService;
import com.ecode.service.UserService;
import com.ecode.utils.EUtil;
import com.ecode.vo.OAuthRegisterVO;
import com.ecode.vo.ScanVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private OauthIdentitiesService oauthIdentitiesService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO.getEmailCode() != null) {
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
                    .role(userRegisterDTO.getRole())
                    .name("默认用户")
                    .profilePicture("default-profile-pic.jpg")
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .score(0L)
                    .build();

            BeanUtils.copyProperties(userRegisterDTO, user);
            userMapper.insert(user);
        } else {
            // 通过OAuth2 注册码注册
            OAuthRegisterVO ov = (OAuthRegisterVO) redisTemplate.opsForValue().get(Redis.OAUTH2_REGISTER_CODE + userRegisterDTO.getRegisterCode());
            if (ov == null || !ov.getRegisterCode().equals(userRegisterDTO.getRegisterCode())){
                throw new RegisterException(MessageConstant.REGISTRATION_FAILED);
            }
            User user = ov.getUserOauthVO().getUser();
            OauthIdentities oauthIdentities = ov.getUserOauthVO().getOauthIdentities();
            User build = User.builder()
                    .username(userRegisterDTO.getUsername())
                    .password(userRegisterDTO.getPassword())
                    .role(userRegisterDTO.getRole())
                    .email(user.getEmail())
                    .address(user.getAddress())
                    .profilePicture(user.getProfilePicture())
                    .name(user.getName())
                    .status(UserStatus.ENABLE)
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .score(0L)
                    .build();
            try {
                userMapper.insert(build);
                oauthIdentities.setUserId(build.getId());
                oauthIdentitiesService.insertOauthIdentities(oauthIdentities);
                redisTemplate.delete(Redis.OAUTH2_REGISTER_CODE + userRegisterDTO.getRegisterCode());
            }catch (DuplicateKeyException e){
                throw new BaseException(EUtil.duplicateKeyException(e));
            }
        }


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

    @Override
    public User getUserByEmail(String email) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email));
    }

    @Override
    public ScanVO scanGenerate() {
        String sceneId = EUtil.generateUUIDWithoutHyphens();

        ScanData sd = ScanData.builder().status(ScanStatus.WAITING).build();

        redisTemplate.opsForValue().set(Redis.LOGIN_SCAN + sceneId,
                sd,
                Redis.LOGIN_SCAN.getTimeout(),
                Redis.LOGIN_SCAN.getTimeUnit()
        );

        return ScanVO.builder()
                .sceneId(sceneId)
                .timeout(Redis.LOGIN_SCAN.getTimeout())
                .build();
    }
}
