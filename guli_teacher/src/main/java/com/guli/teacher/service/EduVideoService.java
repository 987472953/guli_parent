package com.guli.teacher.service;

import com.guli.teacher.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author guli
 * @since 2020-10-18
 */
public interface EduVideoService extends IService<EduVideo> {

    /**
     * 根据id删除数据，先删除阿里云视频
     * @param id
     * @return
     */
    Boolean removeVideo(String id);

    /**
     * 根据课程Id删除多个视频
     * @param id
     * @return
     */
    Boolean removeVideByCourseId(String id);
}
