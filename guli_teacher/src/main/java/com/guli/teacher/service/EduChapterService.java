package com.guli.teacher.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.teacher.entity.EduChapter;
import com.guli.teacher.entity.vo.OneChapter;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author guli
 * @since 2020-10-13
 */
public interface EduChapterService extends IService<EduChapter> {

    /**
     * 根据课程id返回课程的章节信息
     * @param courseId
     * @return
     */
    List<OneChapter> getOneChapterListById(String courseId);

    /**
     * 根据id保存chapter，title不能重复
     * @param chapter
     * @return
     */
    boolean saveChapter(EduChapter chapter);

    /**
     * 根据id更新chapter，title不能重复
     * @param chapter
     * @return
     */
    boolean updateChapter(EduChapter chapter);

    /**
     * 根据课程ID删除chapter
     * @param id
     * @return
     */
    boolean removeByCourseId(String id);
}
