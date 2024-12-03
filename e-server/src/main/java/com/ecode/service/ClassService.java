package com.ecode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecode.dto.ClassPageQueryDTO;
import com.ecode.entity.Class;
import com.ecode.vo.ClassVO;
import com.ecode.vo.PageVO;

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
     * @param classPageQueryDTO 类页面查询
     * @return Page vo< class vo>
     */
    PageVO<ClassVO> pageQuery(ClassPageQueryDTO classPageQueryDTO);

    /**
     * 班级删除批处理
     *
     * @param ids id
     */
    void deleteBatch(List<Integer> ids);
}
