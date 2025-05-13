package com.ecode.aspect;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ecode.annotation.DataAccessCheck;
import com.ecode.constant.MessageConstant;
import com.ecode.context.BaseContext;
import com.ecode.entity.Class;
import com.ecode.enumeration.OperationType;
import com.ecode.exception.PermissionException;
import com.ecode.mapper.ClassMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 数据访问权限检查切面
 * <p>
 * 该类用于在方法执行前进行数据访问权限的校验，确保操作符合权限要求。
 *
 * @author 竹林听雨
 * @date 2025/05/11
 */
@Aspect
@Component
@Slf4j
public class DataAccessCheckAspect {

    @Autowired
    private ClassMapper classMapper;

    /**
     * 在方法执行前进行数据访问权限校验的切面方法。
     *
     * @param joinPoint 切入点，用于获取方法执行的相关信息
     */
    @Before("@annotation(com.ecode.annotation.DataAccessCheck)")
    public void DataAccessCheck(JoinPoint joinPoint){
        //获取到当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名对象
        DataAccessCheck dataAccessCheck = signature.getMethod().getAnnotation(DataAccessCheck.class);//获得方法上的注解对象
        OperationType operationType = dataAccessCheck.value();
        int index = dataAccessCheck.paramIndex();

        //获取到当前被拦截的方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length <= index){
            return;
        }

        Object entity = args[index];
        switch (operationType){
            case TEACHER_TO_CLASS -> TeacherToClass(entity);
            default -> throw new PermissionException(MessageConstant.TYPE_NOT_FOUND);
        }
    }

    /**
     * 老师对于班级的权限检查
     * @param classIdObj 班级id
     */
    private void TeacherToClass(Object classIdObj){
       log.info("开始老师对于班级的权限检查");
       try {
           Integer classId = (Integer) classIdObj;
           Integer teacherId = BaseContext.getCurrentId();
           Optional.ofNullable(classMapper.selectOne(new LambdaQueryWrapper<Class>()
                   .eq(Class::getId, classId)
                   .eq(Class::getTeacherId, teacherId)
           )).orElseThrow(() -> new PermissionException(MessageConstant.DATA_ACCESS_DENIED));
       } catch (Exception e){
           throw new PermissionException(MessageConstant.DATA_ACCESS_DENIED);
       }
    }
}
