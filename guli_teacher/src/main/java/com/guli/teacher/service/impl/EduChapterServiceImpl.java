package com.guli.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.exception.EduException;
import com.guli.teacher.entity.EduChapter;
import com.guli.teacher.entity.EduVideo;
import com.guli.teacher.entity.vo.OneChapter;
import com.guli.teacher.entity.vo.TwoVideo;
import com.guli.teacher.mapper.EduChapterMapper;
import com.guli.teacher.service.EduChapterService;
import com.guli.teacher.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author guli
 * @since 2020-10-13
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    EduVideoService videoService;

    @Override
    public List<OneChapter> getOneChapterListById(String courseId) {

        /**
         * 根据courseid查询章节
         * 将章节copy给OneChapter
         * 将OneChapter加入到List1<OneChapter>
         *
         *  遍历list1，根据chapterid查询videolist
         *
         *  遍历videolist,将video复制到twoVideo
         *  将twovideo加入到OneChapter的children中
         */

        ArrayList<OneChapter> list = new ArrayList<>();

        QueryWrapper<EduChapter> eduChapterQueryWrapper = new QueryWrapper<>();
        eduChapterQueryWrapper.eq("course_id", courseId);
        eduChapterQueryWrapper.orderByAsc("sort", "id");
        List<EduChapter> chapterList = baseMapper.selectList(eduChapterQueryWrapper);

        for (EduChapter chapter : chapterList) {
            OneChapter oneChapter = new OneChapter();
            BeanUtils.copyProperties(chapter, oneChapter);

            QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
            videoQueryWrapper.eq("chapter_id", oneChapter.getId());
            videoQueryWrapper.orderByAsc("sort", "id");
            List<EduVideo> videoList = videoService.list(videoQueryWrapper);
            for (EduVideo video : videoList) {
                TwoVideo twoVideo = new TwoVideo();
                BeanUtils.copyProperties(video, twoVideo);
                oneChapter.getChildren().add(twoVideo);
            }
            list.add(oneChapter);
        }

        return list;
    }

    @Override
    public boolean saveChapter(EduChapter chapter) {
        if (chapter == null) return false;
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("title", chapter.getTitle());
        wrapper.eq("course_id", chapter.getCourseId());
        Integer integer = baseMapper.selectCount(wrapper);
        if (integer > 0) {
            throw new EduException(20001, "章节名称重复");
        }
        int insert = baseMapper.insert(chapter);
        return insert == 1;
    }

    @Override
    public boolean updateChapter(EduChapter chapter) {
        if (chapter == null) return false;
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("title", chapter.getTitle());
        wrapper.eq("sort", chapter.getSort());
        Integer integer = baseMapper.selectCount(wrapper);
        if (integer > 0) {
            throw new EduException(20001, "章节名称重复");
        }
        int i = baseMapper.updateById(chapter);
        return i == 1;
    }

    @Override
    public boolean removeByCourseId(String id) {

        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", id);
        Integer delete = baseMapper.delete(wrapper);
        return delete > 0;
    }
}
