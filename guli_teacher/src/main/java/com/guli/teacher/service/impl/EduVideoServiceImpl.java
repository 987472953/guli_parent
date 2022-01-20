package com.guli.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.teacher.client.VodClient;
import com.guli.teacher.entity.EduVideo;
import com.guli.teacher.mapper.EduVideoMapper;
import com.guli.teacher.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author guli
 * @since 2020-10-18
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    VodClient vodClient;

    @Override
    public Boolean removeVideo(String id) {

        // TODO 删除阿里云视频
        //查询云端视频id
        EduVideo video = baseMapper.selectById(id);
        if(!StringUtils.isEmpty(video.getVideoSourceId())){
            //进行删除
            vodClient.removeVideo(video.getVideoSourceId());
        }

        int i = baseMapper.deleteById(id);
        return i == 1;
    }

    @Override
    public Boolean removeVideByCourseId(String id) {

        //根据课程ID查询所有视频
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", id);
        wrapper.select("video_source_id"); // 只查询video_source_id
        List<EduVideo> eduVideos = baseMapper.selectList(wrapper);
        //遍历视频，将ID放入list
        List<String> list = new ArrayList<>();
        for (EduVideo video : eduVideos){
            if(!StringUtils.isEmpty(video.getVideoSourceId())){
                list.add(video.getVideoSourceId());
            }
        }
        //若list不为空，则调用vod进行删除
        if(list.size()>0){
            vodClient.removeVideoList(list);
        }
        //删除数据库中数据
        QueryWrapper<EduVideo> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("course_id", id);
        Integer delete = baseMapper.delete(wrapper);

        return null != delete && delete > 0;
    }
}
