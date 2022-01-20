package com.guli.teacher.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.Result;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.service.EduCourseService;
import com.guli.teacher.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("frontteacher")
@CrossOrigin
public class EduFrontTeacherController {

    @Autowired
    EduTeacherService teacherService;

    @Autowired
    EduCourseService courseService;

    /**
     * 分页查询，返回分页详细信息
     * 有element组件就不用传全数据
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("list/{page}/{limit}")
    public Result getAllPageMap(@PathVariable Long page,
                                @PathVariable Long limit) {

        Page<EduTeacher> teacherPage = new Page<>(page, limit);
        Map<String, Object> map = teacherService.getFrontPage(teacherPage);
        return Result.ok().data(map);
    }

    @GetMapping("{id}")
    public Result getTeacherInfoById(@PathVariable String id) {

        //获得讲师信息
        EduTeacher teacher = teacherService.getById(id);

        //获得讲师所讲课程信息
        List<EduCourse> courseList = courseService.getListByTeacherId(id);

        return Result.ok().data("teacher", teacher).data("courseList", courseList);
    }

}
