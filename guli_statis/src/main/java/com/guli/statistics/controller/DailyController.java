package com.guli.statistics.controller;


import com.guli.common.Result;
import com.guli.statistics.service.DailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author guli
 * @since 2020-10-31
 */
@RestController
@RequestMapping("/statistics/daily")
@CrossOrigin
public class DailyController {

    @Autowired
    DailyService dailyService;

    /**
     *根据某一天进行查询并将该天数据插入stat表
     */
    @PostMapping("{day}")
    public Result createStatisticsByDay(@PathVariable String day){
        dailyService.createStatisticsByDay(day);
        return Result.ok();
    }

    /**
     * 根据传入时间和类型返回时间数组和数据数组
     * @param begin
     * @param end
     * @param type
     * @return
     */
    @GetMapping("range/{begin}/{end}/{type}")
    public Result getDataByRange(@PathVariable String begin,
                                 @PathVariable String end,
                                 @PathVariable String type){

        Map<String, Object> map = dailyService.getByRange(begin, end, type);
        return Result.ok().data(map);
    }
}

