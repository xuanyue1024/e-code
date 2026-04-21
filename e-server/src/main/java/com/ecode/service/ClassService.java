package com.ecode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecode.dto.AdminClassCreateDTO;
import com.ecode.dto.AdminClassStudentDTO;
import com.ecode.dto.AdminClassUpdateDTO;
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
     * @param studentId 学生id
     * @param classProblemId 班级题目id
     * @return ProblemStuInfoVO
     */
    ProblemStuInfoVO problemStuInfo(Integer studentId, Integer classProblemId);

    /**
     * 获取班级题目提交情况
     *
     * @param classId 班级id
     * @return List<ClassProblemSubmissionsVO>
     */
    List<ClassProblemSubmissionsVO> submissionsInfo(Integer classId);

    /**
     * 获取班级题目提交情况
     *
     * @param classId 班级id
     * @return List<ClassProblemSubmissionsVO>
     */
    List<ClassStudentRankVO> getClassStudentRank(Integer classId);

    /**
     * 获取班级学生完成不同难度题目分布
     *
     * @param classId 班级id
     * @return List<ClassStudentDifficultyVO>
     */
    List<ClassDifficultyDistributionVO> getClassStudentDifficulty(Integer classId);

    /**
     * 获取班级题目通过率排行榜
     *
     * @param classId 班级id
     * @return List<ClassProblemPassRateVO>
     */
    List<ClassProblemPassRateVO> getClassProblemPassRate(Integer classId);

    /**
     * 获取班级题目难度分布
     *
     * @param classId 班级id
     * @return List<ClassProblemDifficultyNumVO>
     */
    List<ClassProblemDifficultyNumVO> getClassProblemDifficultyNum(Integer classId);

    /**
     * 获取班级题目标签数量
     *
     * @param classId 班级id
     * @return List<ClassProblemTagNumVO>
     */
    List<ClassProblemTagNumVO> getClassProblemTagNum(Integer classId);

    /**
     * 管理员创建班级。
     *
     * @param createDTO 创建参数
     */
    void adminAddClass(AdminClassCreateDTO createDTO);

    /**
     * 管理员查询班级详情。
     *
     * @param id 班级id
     * @return 班级详情
     */
    Class getAdminClassById(Integer id);

    /**
     * 管理员更新班级。
     *
     * @param updateDTO 更新参数
     */
    void adminUpdateClass(AdminClassUpdateDTO updateDTO);

    /**
     * 管理员批量删除班级。
     *
     * @param ids 班级id集合
     */
    void adminDeleteBatch(List<Integer> ids);

    /**
     * 管理员给班级添加学生。
     *
     * @param dto 学生维护参数
     */
    void adminAddStudents(AdminClassStudentDTO dto);

    /**
     * 管理员移除班级学生。
     *
     * @param dto 学生维护参数
     */
    void adminRemoveStudents(AdminClassStudentDTO dto);
}
