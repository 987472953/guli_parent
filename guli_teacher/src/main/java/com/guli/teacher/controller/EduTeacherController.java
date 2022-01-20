package com.guli.teacher.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.Result;
import com.guli.common.ResultCode;
import com.guli.common.exception.EduException;
import com.guli.teacher.config.Swagger2Config;
import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.entity.query.TeacherQuery;
import com.guli.teacher.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author guli
 * @since 2020-10-02
 */
@RestController
@RequestMapping("/teacher")
@Api(tags = Swagger2Config.TAG_1)
@CrossOrigin
public class EduTeacherController {

    @Autowired
    EduTeacherService teacherService;

    @ApiOperation(value = "所有讲师列表")
    @GetMapping("list")
    public Result list() {
        try {
            List<EduTeacher> list = teacherService.list(null);
            return Result.ok().data("items", list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @ApiOperation(value = "根据id删除讲师")
    @DeleteMapping("/{id}")
    //如果在形参前加了其他注释，@PathVariable 必须加
    public Result deleteTeacherById(
            @ApiParam(name = "id", value = "讲师id", required = true)
            @PathVariable String id) {
        try {
            boolean b = teacherService.removeById(id);
            if (b) return Result.ok();
            else return Result.error();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @ApiOperation("分页查询讲师列表")
    @PostMapping("/{page}/{limit}")
    public Result selectTeacherByPage(
            @ApiParam(name = "page", value = "当前分页所在数", required = true)
            @PathVariable Integer page,
            @ApiParam(name = "limit", value = "每页显示数据数", required = true)
            @PathVariable Integer limit,
            @ApiParam(name = "teacherQuery", value = "条件查询", required = false)
            @RequestBody TeacherQuery teacherQuery) {
        try {
            Page<EduTeacher> pageParam = new Page<>(page, limit);

            teacherService.pageQuery(pageParam, teacherQuery);
            List<EduTeacher> teacherList = pageParam.getRecords();
            HashMap<String, Object> map = new HashMap<>();
            map.put("rows", teacherList);
            map.put("total", pageParam.getTotal());
            map.put("current", pageParam.getCurrent());
            map.put("pages", pageParam.getPages());
            map.put("hasNext", pageParam.hasNext());
            map.put("hasPrevious", pageParam.hasPrevious());
            return Result.ok().data(map);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @ApiOperation("新增讲师")
    @PostMapping("save")
    public Result insertTeacher(
            @ApiParam(name = "teacher", value = "讲师对象", required = true)
            @RequestBody EduTeacher teacher) {
        try {
            teacherService.save(teacher);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @ApiOperation("根据ID查询讲师")
    @GetMapping("{id}")
    public Result selectTeacherById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id) {

        EduTeacher teacher = teacherService.getById(id);

        if (teacher == null) {
            throw new EduException(ResultCode.EDU_ID_ERROR, "没有该讲师信息");
        }

        return Result.ok().data("item", teacher);

    }

    @ApiOperation("根据ID修改讲师")
    @PutMapping("{id}")
    public Result updateTeacher(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id,
            @ApiParam(name = "teacher", value = "讲师信息", required = true)
            @RequestBody EduTeacher teacher) {
        try {
            teacher.setId(id);
            teacherService.updateById(teacher);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }
}

