package com.guli.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.statistics.cllient.UcenterClient;
import com.guli.statistics.entity.Daily;
import com.guli.statistics.mapper.DailyMapper;
import com.guli.statistics.service.DailyService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author guli
 * @since 2020-10-31
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Autowired
    UcenterClient ucenterClient;

    @Override
    public void createStatisticsByDay(String day) {

        Integer registerCount = (Integer) ucenterClient.getRegisterNumByDay(day).getData().get("registerCount");

        //先进行重复数据删除
        QueryWrapper<Daily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated", day);
        baseMapper.delete(wrapper);

        //将获得数据进行插入
        Daily daily = new Daily();
        daily.setDateCalculated(day);
        daily.setRegisterNum(registerCount);
        //TODO 其他数据
        daily.setCourseNum(RandomUtils.nextInt(100, 200));
        daily.setLoginNum(RandomUtils.nextInt(100, 200));
        daily.setVideoViewNum(RandomUtils.nextInt(100, 200));

        int insert = baseMapper.insert(daily);
    }

    @Override
    public Map<String, Object> getByRange(String begin, String end, String type) {
        QueryWrapper<Daily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end);
        //只查询type列和记录时间
        wrapper.select(type, "date_calculated");
        List<Daily> dailies = baseMapper.selectList(wrapper);

        List<String> dayList = new ArrayList<>();
        List<Integer> numList = new ArrayList<>();
        for (Daily daily : dailies) {
            String dateCalculated = daily.getDateCalculated();
            dayList.add(dateCalculated);
            switch (type) {
                case "login_num":
                    Integer loginNum = daily.getLoginNum();
                    numList.add(loginNum);
                    break;
                case "course_num":
                    Integer courseNum = daily.getCourseNum();
                    numList.add(courseNum);
                    break;
                case "register_num":
                    Integer registerNum = daily.getRegisterNum();
                    numList.add(registerNum);
                    break;
                case "video_view_num":
                    Integer videoViewNum = daily.getVideoViewNum();
                    numList.add(videoViewNum);
                    break;
                default:
                    break;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("dayList", dayList);
        map.put("numList", numList);
        return map;
    }

}
