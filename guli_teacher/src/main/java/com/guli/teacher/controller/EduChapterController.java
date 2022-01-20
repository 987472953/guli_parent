package com.guli.teacher.controller;


import com.guli.common.Result;
import com.guli.teacher.entity.EduChapter;
import com.guli.teacher.entity.vo.OneChapter;
import com.guli.teacher.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author guli
 * @since 2020-10-13
 */
@RestController
@RequestMapping("chapter")
@CrossOrigin
public class EduChapterController {

    @Autowired
    EduChapterService chapterService;

    /**
     * 根据id查询章节，小节列表
     */
    @GetMapping("/list/{courseId}")
    public Result getListById(@PathVariable String courseId) {

        List<OneChapter> list;

        list = chapterService.getOneChapterListById(courseId);

        return Result.ok().data("list", list);
    }

    /**
     * 添加chapter
     *
     * @param chapter
     * @return
     */
    @PostMapping("save")
    public Result saveChapter(@RequestBody EduChapter chapter) {
        boolean save = chapterService.saveChapter(chapter);
        if (save) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    @PutMapping("update")
    public Result updateChapterById(@RequestBody EduChapter chapter) {
        boolean save = chapterService.updateChapter(chapter);
        if (save) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    /**
     * 根据id查询章节，回显等
     *
     * @param courseId
     * @return
     */
    @GetMapping("{courseId}")
    public Result getChapterById(@PathVariable String courseId) {
        try {
            EduChapter chapter = chapterService.getById(courseId);
            return Result.ok().data("chapter", chapter);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @DeleteMapping("{chapterId}")
    public Result deleteById(@PathVariable String chapterId) {
        boolean b = chapterService.removeById(chapterId);
        if (b) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }
}

