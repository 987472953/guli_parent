package com.guli.statistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.statistics.entity.Daily;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author guli
 * @since 2020-10-31
 */
public interface DailyService extends IService<Daily> {

    void createStatisticsByDay(String day);

    Map<String, Object> getByRange(String begin, String end, String type);
}
