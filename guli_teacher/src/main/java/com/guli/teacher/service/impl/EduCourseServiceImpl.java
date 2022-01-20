package com.guli.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.exception.EduException;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.EduCourseDescription;
import com.guli.teacher.entity.query.CourseQuery;
import com.guli.teacher.entity.vo.CourseDesc;
import com.guli.teacher.entity.vo.CoursePublishVo;
import com.guli.teacher.entity.vo.CourseWebVo;
import com.guli.teacher.mapper.EduCourseMapper;
import com.guli.teacher.service.EduChapterService;
import com.guli.teacher.service.EduCourseDescriptionService;
import com.guli.teacher.service.EduCourseService;
import com.guli.teacher.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author guli
 * @since 2020-10-13
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    EduCourseDescriptionService courseDescriptionService;

    @Override
    public String saveVo(CourseDesc vo) {

        int insert = baseMapper.insert(vo.getEduCourse());
        if (insert <= 0) {
            throw new EduException(20001, "添加课程失败");
        }
        String courseId = vo.getEduCourse().getId();
        vo.getEduCourseDescription().setId(courseId);
        boolean save = courseDescriptionService.save(vo.getEduCourseDescription());
        if (!save) {
            throw new EduException(20001, "添加课程描述失败");
        }

        return courseId;
    }

    @Override
    public CourseDesc getShowById(String id) {

        CourseDesc courseDesc = new CourseDesc();

        EduCourse eduCourse = baseMapper.selectById(id);
        if (eduCourse == null) {
            return courseDesc;
        }
        courseDesc.setEduCourse(eduCourse);

        EduCourseDescription courseDescription = courseDescriptionService.getById(id);
        if (courseDescription == null) {
            return courseDesc;
        }
        courseDesc.setEduCourseDescription(courseDescription);

        return courseDesc;
    }

    @Override
    public Boolean updateVo(CourseDesc vo) {

        if (StringUtils.isEmpty(vo.getEduCourse().getId())) {
            throw new EduException(20001, "没有课程编号，修改失败");
        }
        int i = baseMapper.updateById(vo.getEduCourse());
        if (i <= 0) {
            throw new EduException(20001, "修改课程失败");
        }
        //设置课程描述ID
        vo.getEduCourseDescription().setId(vo.getEduCourse().getId());

        return courseDescriptionService.updateById(vo.getEduCourseDescription());
    }

    @Override
    public void getPage(Page<EduCourse> pageParam, CourseQuery courseQuery) {

        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();

        String subjectId = courseQuery.getSubjectId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String teacherId = courseQuery.getTeacherId();
        String title = courseQuery.getTitle();

        if (!StringUtils.isEmpty(subjectParentId)) {
            wrapper.eq("subject_parent_id", subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            wrapper.eq("subject_id", subjectId);
        }
        if (!StringUtils.isEmpty(teacherId)) {
            wrapper.eq("teacher_id", teacherId);
        }
        if (!StringUtils.isEmpty(title)) {
            wrapper.like("title", title);
        }

        baseMapper.selectPage(pageParam, wrapper);
        //page不需要返回
    }

    @Autowired
    EduVideoService videoService;

    @Autowired
    EduChapterService chapterService;

    @Override
    public Boolean removeCourse(String id) {
        //TODO 根据id删除所有视频
        videoService.removeVideByCourseId(id);
        //TODO 根据id删除所有章节
        chapterService.removeByCourseId(id);

        //删除课程描述
        boolean b = courseDescriptionService.removeById(id);
        if (!b) {// 如果描述没有删除成功直接返回
            return false;
        }
        Integer result = baseMapper.deleteById(id);

        return result == 1;

    }

    @Override
    public CoursePublishVo getCoursePublishVoById(String id) {

        CoursePublishVo vo = baseMapper.getCoursePublishVoById(id);
        return vo;
    }

    @Override
    public Boolean updateStatus(String id) {

        EduCourse course = new EduCourse();
        course.setId(id);
        course.setStatus("Normal");
        int i = baseMapper.updateById(course);
        return i == 1;
    }

    @Override
    public Map<String, Object> getMapById(String id) {

        Map<String, Object> map = baseMapper.getMapById(id);
        return map;
    }

    @Override
    public List<EduCourse> getListByTeacherId(String id) {

        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id", id);
        wrapper.orderByDesc("gmt_modified");

        List<EduCourse> courseList = baseMapper.selectList(wrapper);
        return courseList;

    }

    @Override
    public Map<String, Object> getCoursePageMap(Page<EduCourse> coursePage) {

        baseMapper.selectPage(coursePage,null);

        long total = coursePage.getTotal(); // 总记录数
        long current = coursePage.getCurrent(); // 当前页数 page
        long size = coursePage.getSize(); // 每页显示数 limit
        long pages = coursePage.getPages(); //  总页数
        List<EduCourse> records = coursePage.getRecords(); // 数据
        boolean hasNext = coursePage.hasNext();
        boolean hasPrevious = coursePage.hasPrevious();


        HashMap<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("current", current);
        map.put("size", size);
        map.put("pages", pages);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        map.put("records", records);

        return map;
    }

    @Override
    public CourseWebVo getCourseInfo(String courseId) {

        CourseWebVo courseWebVo = baseMapper.getCourseWebInfo(courseId);
        return courseWebVo;
    }


}
