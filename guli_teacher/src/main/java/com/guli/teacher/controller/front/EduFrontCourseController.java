package com.guli.teacher.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.Result;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.vo.CourseWebVo;
import com.guli.teacher.entity.vo.OneChapter;
import com.guli.teacher.service.EduChapterService;
import com.guli.teacher.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("frontcourse")
public class EduFrontCourseController {

    @Autowired
    EduCourseService courseService;

    /**
     * 分页课程数据
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("page/{page}/{limit}")
    public Result getPageMap(@PathVariable Long page,
                             @PathVariable Long limit){
        Page<EduCourse> coursePage = new Page<EduCourse>(page, limit);

        Map<String, Object> map = courseService.getCoursePageMap(coursePage);

        return Result.ok().data(map);

    }

    @Autowired
    private EduChapterService chapterService;

    @GetMapping("courseInfo/{courseId}")
    public Result getCourseInfo(@PathVariable String courseId){

        //查询课程详细信息
        CourseWebVo courseInfo = courseService.getCourseInfo(courseId);

        //获得章节信息
        List<OneChapter> oneChapterList = chapterService.getOneChapterListById(courseId);

        return Result.ok().data("courseInfo", courseInfo).data("oneChapterList", oneChapterList);
    }

}
