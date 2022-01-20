package com.guli.teacher.controller;


import com.guli.common.Result;
import com.guli.teacher.entity.EduVideo;
import com.guli.teacher.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author guli
 * @since 2020-10-18
 */
@RestController
@RequestMapping("video")
@CrossOrigin
public class EduVideoController {

    @Autowired
    EduVideoService videoService;

    /**
     * 增删改查
     */

    @PostMapping("save")
    public Result saveById(@RequestBody EduVideo video) {
        boolean save = videoService.save(video);
        if (save) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    @DeleteMapping("{id}")
    public Result removeById(@PathVariable String id) {
        Boolean b = videoService.removeVideo(id);
        if (b) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    @PutMapping("update")
    public Result updateVideo(@RequestBody EduVideo video) {

        boolean b = videoService.updateById(video);
        if (b) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    @GetMapping("{id}")
    public Result getById(@PathVariable String id) {
        EduVideo video = videoService.getById(id);
        return Result.ok().data("video", video);
    }
}

