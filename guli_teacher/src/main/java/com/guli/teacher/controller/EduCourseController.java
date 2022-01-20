package com.guli.teacher.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.Result;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.query.CourseQuery;
import com.guli.teacher.entity.vo.CourseDesc;
import com.guli.teacher.entity.vo.CoursePublishVo;
import com.guli.teacher.service.EduCourseService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author guli
 * @since 2020-10-13
 */
@RestController
@RequestMapping("course")
@CrossOrigin
public class EduCourseController {

    @Autowired
    EduCourseService courseService;

    @PostMapping("save")
    public Result saveCourse(@RequestBody CourseDesc vo){

        try {
            String courseId = courseService.saveVo(vo);
            return Result.ok().data("id", courseId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error().data("message","保存数据失败");
        }
    }

    /**
     * 根据id进行回显
     */
    @GetMapping("{id}")
    public Result getCourseVoById(@PathVariable String id){

        try {
            CourseDesc courseDesc = courseService.getShowById(id);
            return Result.ok().data("courseInfo", courseDesc);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error().data("msg","获得回显数据失败");
        }
    }

    /**
     * 根据id进行修改
     */
    @PutMapping("update")
    public Result upadateVo(@RequestBody CourseDesc vo){
        Boolean flag = courseService.updateVo(vo);
        if(flag){
            return Result.ok();
        }else{
            return Result.error();
        }
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("{page}/{limit}")
    public Result selectCourseList(@ApiParam(name = "page", value = "当前页数", required = true)
                                   @PathVariable Long page,
                                   @ApiParam(name = "limit", value = "每页记录数", required = true)
                                   @PathVariable Long limit,
                                   @ApiParam(name = "courseQuery", value = "查询条件", required = false)
                                   @RequestBody CourseQuery courseQuery){

        Page<EduCourse> pageParam = new Page<>(page, limit);
        courseService.getPage(pageParam, courseQuery);

        return Result.ok().data("total", pageParam.getTotal())
                .data("rows", pageParam.getRecords());
    }

    @ApiOperation(value = "根据ID删除课程")
    @DeleteMapping("{id}")
    public Result deleteById(@ApiParam(name = "id", value = "课程ID", required = true)
                            @PathVariable String id){

        Boolean flag = courseService.removeCourse(id);
        if(flag){
            return Result.ok();
        }else{
            return Result.error();
        }
    }

//    @GetMapping("vo/{id}")
//    public Result getVoById(@PathVariable String id){
//       CoursePublishVo vo = courseService.getCoursePublishVoById(id);
//        System.out.println(vo);
//       if(vo==null){
//           return Result.error().data("message", "获得数据失败");
//       }else{
//           return Result.ok().data("vo", vo);
//       }
//    }

    /**
     * 第二种方式 (简单)
     * @param id
     * @return
     */
    @GetMapping("vo/{id}")
    public Result getVoById(@PathVariable String id){

        Map<String, Object> map = courseService.getMapById(id);
        return Result.ok().data(map);
    }

    @PutMapping("status/{id}")
    public Result updateStatus(@PathVariable String id){
        Boolean flag = courseService.updateStatus(id);
        if(flag){
            return Result.ok();
        }else{
            return Result.error();
        }
    }
}

