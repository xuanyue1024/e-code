package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.dto.AdminUserCreateDTO;
import com.ecode.dto.AdminUserPageQueryDTO;
import com.ecode.dto.AdminUserUpdateDTO;
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
import com.ecode.service.AdminSessionService;
import com.ecode.service.OauthIdentitiesService;
import com.ecode.service.UserService;
import com.ecode.utils.EUtil;
import com.ecode.utils.IpUtil;
import com.ecode.utils.S3Util;
import com.ecode.vo.AdminUserVO;
import com.ecode.vo.OAuthRegisterVO;
import com.ecode.vo.PageVO;
import com.ecode.vo.ScanVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

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

    @Autowired
    private S3Util s3Util;

    @Autowired
    private AdminSessionService adminSessionService;

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
    public String getProfilePictureById(Integer id) {
        User u = this.getOne(new LambdaQueryWrapper<User>().select(User::getProfilePicture).eq(User::getId, id));
        if (u == null){
            throw new BaseException(MessageConstant.USER_NOT_FOUND);
        }

        return s3Util.getPublicUrl(u.getProfilePicture());
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email));
    }

    @Override
    public ScanVO scanGenerate() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) requireNonNull(RequestContextHolder
                .getRequestAttributes());
        HttpServletRequest request = requestAttributes.getRequest();

        String sceneId = EUtil.generateUUIDWithoutHyphens();
        String ipAddr = IpUtil.getIpAddr(request);

        ScanData sd = ScanData.builder()
                .status(ScanStatus.WAITING)
                .ip(ipAddr)
                .build();

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

    @Override
    public PageVO<AdminUserVO> adminPage(AdminUserPageQueryDTO queryDTO) {
        Page<User> page = queryDTO.toMpPage(OrderItem.desc("update_time"));
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDTO.getKeyword())) {
            wrapper.and(w -> w
                    .like(User::getName, queryDTO.getKeyword())
                    .or()
                    .like(User::getUsername, queryDTO.getKeyword())
                    .or()
                    .like(User::getEmail, queryDTO.getKeyword()));
        }
        wrapper.eq(queryDTO.getRole() != null, User::getRole, queryDTO.getRole());
        wrapper.eq(queryDTO.getStatus() != null, User::getStatus, queryDTO.getStatus());

        Page<User> userPage = userMapper.selectPage(page, wrapper);
        List<User> records = userPage.getRecords();
        if (records == null || records.isEmpty()) {
            return new PageVO<>(userPage.getTotal(), userPage.getPages(), Collections.emptyList());
        }

        List<AdminUserVO> list = records.stream().map(this::toAdminUserVO).collect(Collectors.toList());
        return new PageVO<>(userPage.getTotal(), userPage.getPages(), list);
    }

    @Override
    public AdminUserVO adminGetById(Integer id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BaseException(MessageConstant.USER_NOT_FOUND);
        }
        return toAdminUserVO(user);
    }

    @Override
    public void adminCreate(AdminUserCreateDTO createDTO) {
        User user = new User();
        BeanUtils.copyProperties(createDTO, user);
        user.setStatus(createDTO.getStatus() == null ? UserStatus.ENABLE : createDTO.getStatus());
        user.setName(StringUtils.hasText(createDTO.getName()) ? createDTO.getName() : "默认用户");
        user.setProfilePicture(StringUtils.hasText(createDTO.getProfilePicture()) ? createDTO.getProfilePicture() : "default-profile-pic.jpg");
        user.setScore(createDTO.getScore() == null ? 0L : createDTO.getScore());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        try {
            userMapper.insert(user);
        } catch (DuplicateKeyException e) {
            throw new BaseException(EUtil.duplicateKeyException(e));
        }
    }

    @Override
    public void adminUpdate(AdminUserUpdateDTO updateDTO) {
        if (userMapper.selectById(updateDTO.getId()) == null) {
            throw new BaseException(MessageConstant.USER_NOT_FOUND);
        }
        User user = new User();
        BeanUtils.copyProperties(updateDTO, user);
        if (!StringUtils.hasText(updateDTO.getPassword())) {
            user.setPassword(null);
        }
        user.setUpdateTime(LocalDateTime.now());
        try {
            int rows = userMapper.updateById(user);
            if (rows <= 0) {
                throw new BaseException(MessageConstant.UPDATE_FAILED);
            }
        } catch (DuplicateKeyException e) {
            throw new BaseException(EUtil.duplicateKeyException(e));
        }
    }

    @Override
    public void adminUpdateStatus(Integer id, UserStatus status) {
        User user = User.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .build();
        int rows = userMapper.updateById(user);
        if (rows <= 0) {
            throw new BaseException(MessageConstant.USER_NOT_FOUND);
        }
        if (status == UserStatus.DISABLE) {
            adminSessionService.logoutUser(id);
        }
    }

    @Override
    public void adminDeleteBatch(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BaseException(MessageConstant.PARAMETER_ERROR);
        }
        userMapper.deleteBatchIds(ids);
        ids.forEach(adminSessionService::logoutUser);
    }

    private AdminUserVO toAdminUserVO(User user) {
        AdminUserVO vo = new AdminUserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
