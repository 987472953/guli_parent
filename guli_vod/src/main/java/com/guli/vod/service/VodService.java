package com.guli.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    /**
     * 上传视频，返回视频ID
     * @param file
     * @return
     */
    String uploadVideo(MultipartFile file);

    /**
     * 根据id删除云端视频
     * @param videoSourceId
     * @return
     */
    Boolean removeVideoById(String videoSourceId);

    /**
     *
     * 根据list删除云端视频
     * @param videoList
     * @return
     */
    Boolean removeVideoList(List<String> videoList);
}
