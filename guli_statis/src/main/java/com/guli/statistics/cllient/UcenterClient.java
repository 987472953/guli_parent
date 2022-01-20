package com.guli.statistics.cllient;

import com.guli.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("guli-ucenter")
public interface UcenterClient {

    /**
     * 根据天数获得当天注册人数
     * @param day
     * @return
     */
    @GetMapping("/ucenter/member/count/{day}")
    public Result getRegisterNumByDay(@PathVariable("day") String day);
}
