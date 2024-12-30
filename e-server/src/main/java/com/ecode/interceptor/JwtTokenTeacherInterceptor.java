package com.ecode.interceptor;

import com.alibaba.fastjson.JSON;
import com.ecode.constant.JwtClaimsConstant;
import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.enumeration.UserRole;
import com.ecode.exception.PermissionException;
import com.ecode.properties.JwtProperties;
import com.ecode.result.Result;
import com.ecode.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenTeacherInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());

        //2、校验令牌
        Claims claims;
        try {
            log.info("jwt校验:{}", token);
            claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(401);
            response.getWriter().write(JSON.toJSONString(Result.error(MessageConstant.JWT_FAILS)));
            return false;
        }
        Integer empId = Integer.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
        UserRole role = UserRole.fromDesc((String) claims.get(JwtClaimsConstant.ROLE));
        log.info("当前用户id：{}，角色:{}", empId, role.getDesc());

        if (role != UserRole.TEACHER) throw new PermissionException(MessageConstant.ACCESS_DENIED);
        BaseContext.setCurrentRole(role);
        BaseContext.setCurrentId(empId);
        //3、通过，放行
        return true;
    }
}
