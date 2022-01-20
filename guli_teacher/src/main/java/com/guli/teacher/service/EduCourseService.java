package com.guli.teacher.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.teacher.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.teacher.entity.query.CourseQuery;
import com.guli.teacher.entity.vo.CourseDesc;
import com.guli.teacher.entity.vo.CoursePublishVo;
import com.guli.teacher.entity.vo.CourseWebVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author guli
 * @since 2020-10-13
 */
public interface EduCourseService extends IService<EduCourse> {

    /**
     * 保存课程信息和课程描述
     * @param vo
     * @return 课程id
     */
    String saveVo(CourseDesc vo);

    /**
     * 根据id回显数据
     * @param id
     * @return
     */
    CourseDesc getShowById(String id);

    /**
     * 根据id修course和courseDescription
     * @param vo
     * @return
     */
    Boolean updateVo(CourseDesc vo);

    /**
     * 根据条件查询分页，查询条件可能为空
     * @param pageParam
     * @param courseQuery
     */
    void getPage(Page<EduCourse> pageParam, CourseQuery courseQuery);

    /**
     * 根据id删除相关信息
     * @param id
     * @return
     */
    Boolean removeCourse(String id);

    /**
     * 根据id获得该课程的详细信息
     * @param id
     * @return
     */
    CoursePublishVo getCoursePublishVoById(String id);

    /**
     * 发布后修改发布状态为Normal
     * @param id
     * @return
     */
    Boolean updateStatus(String id);

    Map<String, Object> getMapById(String id);

    /**
     * 根据讲师id获得课程列表
     * @param id
     * @return
     */
    List<EduCourse> getListByTeacherId(String id);

    /**
     * 根据分页条件查询
     * @param coursePage
     * @return
     */
    Map<String, Object> getCoursePageMap(Page<EduCourse> coursePage);

    /**
     * 获得前端展示的课程详细信息
     * @param courseId
     * @return
     */
    CourseWebVo getCourseInfo(String courseId);
}
