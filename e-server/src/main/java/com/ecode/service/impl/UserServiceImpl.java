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
import com.ecode.enumeration.UserRole;
import com.ecode.enumeration.UserSex;
import com.ecode.enumeration.UserStatus;
import com.ecode.exception.BaseException;
import com.ecode.exception.RegisterException;
import com.ecode.json.ScanData;
import com.ecode.mapper.UserMapper;
import com.ecode.service.AdminSessionService;
import com.ecode.service.OauthIdentitiesService;
import com.ecode.service.UserService;
import com.ecode.utils.EUtil;
import com.ecode.utils.ExcelUtil;
import com.ecode.utils.IpUtil;
import com.ecode.utils.S3Util;
import com.ecode.vo.AdminUserVO;
import com.ecode.vo.ImportResultVO;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    private static final List<String> USER_EXCEL_HEADERS = List.of(
            "id", "username", "password", "role", "email", "status", "name",
            "profilePicture", "phone", "sex", "address", "score", "birthDate");

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

    @Override
    public byte[] exportUsers() {
        List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().orderByDesc(User::getUpdateTime));
        List<List<Object>> rows = users.stream()
                .map(user -> List.<Object>of(
                        valueOrBlank(user.getId()),
                        valueOrBlank(user.getUsername()),
                        "",
                        valueOrBlank(user.getRole()),
                        valueOrBlank(user.getEmail()),
                        valueOrBlank(user.getStatus()),
                        valueOrBlank(user.getName()),
                        valueOrBlank(user.getProfilePicture()),
                        valueOrBlank(user.getPhone()),
                        valueOrBlank(user.getSex()),
                        valueOrBlank(user.getAddress()),
                        valueOrBlank(user.getScore()),
                        valueOrBlank(user.getBirthDate())))
                .toList();
        return ExcelUtil.write("用户", USER_EXCEL_HEADERS, rows);
    }

    @Override
    public byte[] exportUserTemplate() {
        return ExcelUtil.write("用户导入模板", USER_EXCEL_HEADERS, List.of(List.of(
                "", "student001", "123456", "STUDENT", "student001@example.com", "ENABLE",
                "学生一", "default-profile-pic.jpg", "", "MALE", "", "0", "2000-01-01"
        )));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImportResultVO importUsers(MultipartFile file) {
        List<Map<String, String>> rows = ExcelUtil.read(file, List.of("username", "password", "role", "email"));
        ImportResultVO result = new ImportResultVO();
        result.setTotal(rows.size());
        Set<String> seenUsernames = new HashSet<>();
        Set<String> seenEmails = new HashSet<>();

        for (Map<String, String> row : rows) {
            int rowNumber = Integer.parseInt(row.get("__rowNumber"));
            try {
                String username = row.get("username");
                String email = row.get("email");
                if (!StringUtils.hasText(username) || !StringUtils.hasText(email)
                        || !StringUtils.hasText(row.get("password")) || !StringUtils.hasText(row.get("role"))) {
                    result.addFailed(rowNumber, "用户名、密码、角色、邮箱不能为空");
                    continue;
                }
                if (!seenUsernames.add(username) || !seenEmails.add(email)) {
                    result.addSkipped(rowNumber, "文件内用户名或邮箱重复");
                    continue;
                }
                if (userExists(username, email)) {
                    result.addSkipped(rowNumber, "用户名或邮箱已存在");
                    continue;
                }

                User user = User.builder()
                        .username(username)
                        .password(row.get("password"))
                        .role(UserRole.valueOf(row.get("role")))
                        .email(email)
                        .status(StringUtils.hasText(row.get("status")) ? UserStatus.valueOf(row.get("status")) : UserStatus.ENABLE)
                        .name(StringUtils.hasText(row.get("name")) ? row.get("name") : "默认用户")
                        .profilePicture(StringUtils.hasText(row.get("profilePicture")) ? row.get("profilePicture") : "default-profile-pic.jpg")
                        .phone(blankToNull(row.get("phone")))
                        .sex(StringUtils.hasText(row.get("sex")) ? UserSex.valueOf(row.get("sex")) : null)
                        .address(blankToNull(row.get("address")))
                        .score(StringUtils.hasText(row.get("score")) ? Long.valueOf(row.get("score")) : 0L)
                        .birthDate(StringUtils.hasText(row.get("birthDate")) ? LocalDate.parse(row.get("birthDate")) : null)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build();
                userMapper.insert(user);
                result.addCreated(rowNumber, "新增用户：" + username);
            } catch (IllegalArgumentException | DateTimeParseException e) {
                result.addFailed(rowNumber, "枚举、日期或数字格式错误");
            } catch (Exception e) {
                result.addFailed(rowNumber, e.getMessage());
            }
        }
        return result;
    }

    private AdminUserVO toAdminUserVO(User user) {
        AdminUserVO vo = new AdminUserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    private boolean userExists(String username, String email) {
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .or()
                .eq(User::getEmail, email));
        return count != null && count > 0;
    }

    private Object valueOrBlank(Object value) {
        return value == null ? "" : value;
    }

    private String blankToNull(String value) {
        return StringUtils.hasText(value) ? value : null;
    }
}
