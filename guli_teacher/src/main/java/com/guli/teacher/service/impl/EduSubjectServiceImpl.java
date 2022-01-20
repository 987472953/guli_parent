package com.guli.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.teacher.entity.EduSubject;
import com.guli.teacher.entity.vo.OneSubject;
import com.guli.teacher.entity.vo.TwoSubject;
import com.guli.teacher.mapper.EduSubjectMapper;
import com.guli.teacher.service.EduSubjectService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author guli
 * @since 2020-10-11
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public List<String> importExcel(MultipartFile file) {

        List<String> msgList = new ArrayList<>();


        try {
            InputStream inputStream = file.getInputStream();

            //1.打开Excel文件
            Workbook workbook = new HSSFWorkbook(inputStream);

            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            //2.从第1行开始将0列数据拿出，判断是否为空，并将数据库数据取出并比较，当数据库没有该项时插入
            for (int rowNum = 1; rowNum <= lastRowNum; rowNum++) {
                Row rowData = sheet.getRow(rowNum);
                //根据每一行获取列表数据
                //获取第一列
                Cell cellOne = rowData.getCell(0);
                //根据cell获取第一列里面内容
                if (cellOne == null) {
                    msgList.add("第" + rowNum + "行第一列数据为空");
                    //跳到下一行进行操作
                    continue;
                }
                String cellOneValue = cellOne.getStringCellValue();
                if (StringUtils.isEmpty(cellOneValue)) {
                    msgList.add("第" + rowNum + "行第一列数据为空");
                    //跳到下一行进行操作
                    continue;
                }

                //cellOneValue 是一级分类名称
                //判断添加的一级分类名称在数据库表里面是否有相同的，如果没有添加
                EduSubject eduSubject = this.existOneSubject(cellOneValue);
                //为了后面添加二级分类，一级分类id
                String pid = null;
                if (eduSubject == null) { //表没有相同的一级分类
                    //添加到数据库里面
                    EduSubject subjectOne = new EduSubject();
                    subjectOne.setTitle(cellOneValue);
                    subjectOne.setParentId("0");
                    baseMapper.insert(subjectOne);
                    //添加一级分类之后，获取添加之后id值
                    pid = subjectOne.getId();
                } else {
                    //有相同的一级分类
                    pid = eduSubject.getId();
                }

                //获取第二列
                Cell cellTwo = rowData.getCell(1);
                if (cellTwo == null) {
                    msgList.add("第" + rowNum + "行第二列数据为空");
                    continue;
                }
                // 根据cell获取第二列里面内容
                String cellTwoValue = cellTwo.getStringCellValue();
                if (StringUtils.isEmpty(cellTwoValue)) {
                    msgList.add("第" + rowNum + "行第二列数据为空");
                    continue;
                }
                //cellTwoValue 是二级分类名称
                EduSubject eduSubjectTwo = this.existTwoSubject(cellTwoValue, pid);
                if (eduSubjectTwo == null) {
                    //没有相同的二级分类，实现二级分类添加
                    EduSubject subjectTwo = new EduSubject();
                    subjectTwo.setTitle(cellTwoValue);
                    subjectTwo.setParentId(pid);
                    baseMapper.insert(subjectTwo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msgList;
    }

    @Override
    public List<OneSubject> getTree() {

        ArrayList<OneSubject> subjectList = new ArrayList<>();
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();

        //查新一级菜单到oneList
        wrapper.eq("parent_id","0");
        List<EduSubject> oneList = baseMapper.selectList(wrapper);
        for(EduSubject subject: oneList){

            OneSubject oneSubject = new OneSubject();
            BeanUtils.copyProperties(subject,oneSubject);
            subjectList.add(oneSubject);
            //遍历list根据id与pid的关系查询二级菜单
            QueryWrapper<EduSubject> wrapperSon = new QueryWrapper<>();
            wrapperSon.eq("parent_id",oneSubject.getId());
            List<EduSubject> eduSubjects = baseMapper.selectList(wrapperSon);
            for(EduSubject subjectSon: eduSubjects){
                TwoSubject twoSubject = new TwoSubject();
                BeanUtils.copyProperties(subjectSon, twoSubject);
                oneSubject.getChildren().add(twoSubject);
            }
        }


        return subjectList;
    }

    @Override
    public boolean deleteById(String id) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<EduSubject> subjectList = baseMapper.selectList(wrapper);
        if(subjectList.size() != 0){
            return false;
        }
        int i = baseMapper.deleteById(id);

        return i==1;
    }

    @Override
    public Boolean saveLevelOne(EduSubject eduSubject) {

        EduSubject subject = this.existOneSubject(eduSubject.getTitle());
        if(subject != null){
            return false;
        }
        return super.save(eduSubject);
    }

    @Override
    public Boolean saveLevelTwo(EduSubject eduSubject) {
        EduSubject subject = this.existTwoSubject(eduSubject.getTitle(), eduSubject.getParentId());
        if(subject != null){
            return false;
        }

        return super.save(eduSubject);
    }


    private EduSubject existTwoSubject(String cellTwoValue, String pid) {

        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", cellTwoValue);
        wrapper.eq("parent_id", pid);
        return baseMapper.selectOne(wrapper);
    }

    private EduSubject existOneSubject(String cellOneValue) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<EduSubject>();

        wrapper.eq("title", cellOneValue);
        return baseMapper.selectOne(wrapper);
    }

}
