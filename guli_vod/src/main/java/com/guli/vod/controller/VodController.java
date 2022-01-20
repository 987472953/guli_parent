package com.guli.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.guli.common.Result;
import com.guli.vod.service.VodService;
import com.guli.vod.util.AliyunVODSDKUtils;
import com.guli.vod.util.ConstantPropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("vod")
@CrossOrigin
public class VodController {

    @Autowired
    VodService vodService;

    /**
     * 上传视频
     * @param file
     * @return
     */
    @PostMapping("upload")
    public Result upload(MultipartFile file){

        String videoSourceId = vodService.uploadVideo(file);
        return Result.ok().data("videoSourceId", videoSourceId);
    }

    /**
     * 删除视频
     * @param videoSourceId
     * @return
     */
    @DeleteMapping("{videoSourceId}")
    public Result removeVideo(@PathVariable String videoSourceId){
        Boolean flag = vodService.removeVideoById(videoSourceId);
        if(flag){
            return Result.ok();
        }else{
            return Result.error();
        }
    }

    /**
     * 根据idList删除多个视频
     * @param videoList
     * @return
     */
    @DeleteMapping("removeList")
    public Result removeVideoList(@RequestParam("videoList") List<String> videoList){
        Boolean flag = vodService.removeVideoList(videoList);
        if(flag){
            return Result.ok();
        }else{
            return Result.error();
        }
    }

    /**
     * 根据视频id获得播放凭证
     */
    @GetMapping("playauth/{videoId}")
    public Result getVideoPlayauth(@PathVariable String videoId){

        try {
            DefaultAcsClient client = AliyunVODSDKUtils.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoId);
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return Result.ok().data("playAuth", playAuth);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

}
