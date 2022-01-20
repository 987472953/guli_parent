package com.guli.teacher.controller;


import com.guli.common.Result;
import com.guli.teacher.entity.EduSubject;
import com.guli.teacher.entity.vo.OneSubject;
import com.guli.teacher.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author guli
 * @since 2020-10-11
 */
@RestController
@RequestMapping("/subject")
@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService subjectService;

    @PostMapping("import")
    public Result importSubject(MultipartFile file){

        List<String> msgList = subjectService.importExcel(file);

        if(msgList.size()==0){
            return Result.ok();
        }else{
            return Result.error().data("msgList", msgList);
        }

    }

    @GetMapping("tree")
    public Result tree(){

        List<OneSubject> subjectList = subjectService.getTree();


        return Result.ok().data("subjectList",subjectList);
    }


    @DeleteMapping("{id}")
    public Result removeById(@PathVariable String id){
        boolean b = subjectService.deleteById(id);
        if(b){
            return Result.ok();
        }else{
            return Result.error();
        }
    }

    @PostMapping("saveLevelOne")
    public Result saveLevelOne(@RequestBody EduSubject eduSubject){

        Boolean flag = subjectService.saveLevelOne(eduSubject);
        if(flag) {
            return Result.ok();
        }else {
            return Result.error();
        }
    }

    @PostMapping("saveLevelTwo")
    public Result saveLevelTwo(@RequestBody EduSubject eduSubject){

        Boolean flag = subjectService.saveLevelTwo(eduSubject);
        if(flag) {
            return Result.ok();
        }else {
            return Result.error();
        }
    }
}

