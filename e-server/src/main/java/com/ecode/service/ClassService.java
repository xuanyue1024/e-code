package com.ecode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecode.dto.ClassProblemDTO;
import com.ecode.dto.ClassProblemPageQueryDTO;
import com.ecode.dto.ClassStudentDTO;
import com.ecode.dto.GeneralPageQueryDTO;
import com.ecode.entity.Class;
import com.ecode.vo.*;

import java.util.List;

/**
 * 班级服务类
 *
 * @author 竹林听雨
 * @date 2024/11/23
 */
public interface ClassService extends IService<Class> {
    /**
     * 添加班级
     *
     * @param name 班级名称
     */
    void addClass(String name);

    /**
     * 班级分页查询
     *
     * @param generalPageQueryDTO 类页面查询
     * @return Page vo< class vo>
     */
    PageVO<ClassVO> pageQuery(Integer teacherId, Integer studentId, GeneralPageQueryDTO generalPageQueryDTO);

    /**
     * 班级删除批处理
     *
     * @param ids id
     */
    void deleteBatch(List<Integer> ids);

    /**
     * 按教师id,班级id更新班级名称
     *
     * @param id        班级id
     * @param teacherId 老师id
     * @param name      班级名称
     */
    void updateNameByIdAndTeacherId(Integer id, Integer teacherId, String name);


    /**
     * 加入班级方法
     *
     * @param studentId      学生id
     * @param invitationCode 邀请码
     */
    void joinClass(Integer studentId, String invitationCode);

    /**
     * 批量退出班级
     *
     * @param studentId 学生id
     * @param classIds  班级id
     */
    void exitBatch(Integer studentId, List<Integer> classIds);

    /**
     * 为班级增加题目
     *
     * @param classProblemDTO 类添加问题
     */
    void addProblemBatch(ClassProblemDTO classProblemDTO);

    /**
     * 批量删除班级内题目
     *
     * @param classProblemDTO 类问题d
     */
    void deleteProblemBatch(ClassProblemDTO classProblemDTO);

    /**
     * 班级问题分页查询
     *
     * @param classProblemPageQueryDTO 类问题页面查询dto
     * @return 页vo<问题页vo>
     */
    PageVO<ProblemPageVO> problemPage(ClassProblemPageQueryDTO classProblemPageQueryDTO);

    /**
     * 班级学生列表
     *
     * @param classStudentDTO 班级学生d
     * @return UserVO
     */
    PageVO<UserVO> studentPage(ClassStudentDTO classStudentDTO);

    /**
     * 获取指定学生指定班级题目的作答信息
     *
     * @param stuId          学生id
     * @param classProblemId 班级问题id
     * @return 问题信息
     */
    ProblemStuInfoVO problemStuInfo(Integer stuId, Integer classProblemId);
}
