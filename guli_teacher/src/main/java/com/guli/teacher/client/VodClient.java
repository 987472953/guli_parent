package com.guli.teacher.client;

import com.guli.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient("guli-vod") // 需要调用的服务的服务名
public interface VodClient {

    /**
     * 调用vod微服务中的删除云端视频
     *        @PathVariable注解一定要指定参数名称，否则出错
     * @param videoSourceId
     * @return
     */
    @DeleteMapping("vod/{videoSourceId}")
    public Result removeVideo(@PathVariable("videoSourceId") String videoSourceId);

    /**
     * 根据list删除视频
     * @param list
     * @return
     */
    @DeleteMapping("vod/removeList")
    public Result removeVideoList(@RequestParam("videoList") List<String> list);
}
