package com.guli.teacher.service;

import com.guli.teacher.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.teacher.entity.vo.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author guli
 * @since 2020-10-11
 */
public interface EduSubjectService extends IService<EduSubject> {

    /**
     * 上传excel添加规定格式内容到edu_subject
     * @param file
     * @return 返回空行信息
     */
    List<String> importExcel(MultipartFile file);

    /**
     * 获得subjectList
     * @return
     */
    List<OneSubject> getTree();

    /**
     * 根据id删除课程
     * @param id
     * @return
     */
    boolean deleteById(String id);

    /**
     * 添加一加分类
     * @param eduSubject
     * @return
     */
    Boolean saveLevelOne(EduSubject eduSubject);

    /**
     * 添加二级分类
     * @param eduSubject
     * @return
     */
    Boolean saveLevelTwo(EduSubject eduSubject);
}
