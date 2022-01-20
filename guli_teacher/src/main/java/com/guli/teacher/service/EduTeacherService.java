package com.guli.teacher.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.entity.query.TeacherQuery;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author guli
 * @since 2020-10-02
 */
public interface EduTeacherService extends IService<EduTeacher> {

    /**
     *  根据多个条件查询讲师列表
     * @param pageParam
     * @param teacherQuery
     */
    void pageQuery(Page<EduTeacher> pageParam, TeacherQuery teacherQuery);

    /**
     * 分页查询所有讲师，返回详细信息（没有element框架， 需要自己写分页）
     * @param teacherPage
     * @return
     */
    Map<String, Object> getFrontPage(Page<EduTeacher> teacherPage);
}
