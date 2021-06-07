package com.atguigu.guli.service.edu.service.impl;


import com.atguigu.guli.common.base.result.ExcelImportUtil;
import com.atguigu.guli.service.edu.entity.Subject;

import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.atguigu.guli.service.edu.service.SubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2019-12-25
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchImport(InputStream inputStream) throws Exception {

        //调用Excel工具类获取工具对象
        ExcelImportUtil importUtil = new ExcelImportUtil(inputStream);

        //获取sheet对象
        HSSFSheet sheet = importUtil.getSheet();

        //解析Excel
        for (Row rowData : sheet) {

            //标题行
            if (rowData.getRowNum() == 0) {
                continue;
            }

            //获取一级分类
            Cell levelOneCell = rowData.getCell(0);
            String levelOneValue = importUtil.getCellValue(levelOneCell).trim();
            if (levelOneCell == null || StringUtils.isEmpty(levelOneValue)) {
                continue;
            }

            //获取二级分类
            Cell levelTwoCell = rowData.getCell(1);
            String levelTwoValue = importUtil.getCellValue(levelTwoCell).trim();
            if (levelTwoCell == null || StringUtils.isEmpty(levelTwoValue)) {
                continue;
            }

            //判断一级类别是否已存在
            Subject subject = this.getByTitle(levelOneValue);
            String parentId = "";
            if (subject == null) { //不存在
                //将一级分类存入数据库
                Subject subjectLevelOne = new Subject();
                subjectLevelOne.setTitle(levelOneValue);
                baseMapper.insert(subjectLevelOne);
                parentId = subjectLevelOne.getId();
            } else {
                parentId = subject.getId();
            }

            //判断二级类别是否已存在
            Subject subjectSub = this.getSubByTitle(levelTwoValue, parentId);

            if (subjectSub == null) {
                //将二级分类存入数据库
                Subject subjectLevelTwo = new Subject();
                subjectLevelTwo.setTitle(levelTwoValue);
                subjectLevelTwo.setParentId(parentId);
                baseMapper.insert(subjectLevelTwo);
            }
        }

    }


    /**
     * 判断一级类别是否存在
     *
     * @param title
     * @return
     */
    private Subject getByTitle(String title) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", 0);
        queryWrapper.eq("title", title);
        return baseMapper.selectOne(queryWrapper);
    }


    /**
     * 判断二级分类是否存在
     * 当一级分类id一致时，数据重复
     *
     * @param title
     * @param parentId
     * @return
     */
    private Subject getSubByTitle(String title, String parentId) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        queryWrapper.eq("title", title);
        return baseMapper.selectOne(queryWrapper);
    }


    @Override
    public List<SubjectVo> nestedList() {
        //最终要得到的数据列表
        ArrayList<SubjectVo> subjectVoList = new ArrayList<>();

        //获取分类数据记录
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort", "id");
        List<Subject> subjectList = baseMapper.selectList(queryWrapper);

        //分别获取一级分类和二级分类的数据
        ArrayList<Subject> subjectlevelOneList = new ArrayList<>();
        ArrayList<Subject> subjectlevelTwoList = new ArrayList<>();
        for (Subject subject : subjectList) {
            if (subject.getParentId().equals("0")) {
                subjectlevelOneList.add(subject);
            } else {
                subjectlevelTwoList.add(subject);
            }

        }

        //填充一级分类的数据
        for (Subject subjectLevelOne : subjectlevelOneList) {
            SubjectVo subjectVolevelOne = new SubjectVo();
            BeanUtils.copyProperties(subjectLevelOne, subjectVolevelOne);
            subjectVoList.add(subjectVolevelOne);

            ArrayList<SubjectVo> subjectVoLevelTwoList = new ArrayList<>();
            for (Subject subjectLevelTwo : subjectlevelTwoList) {
                if (subjectLevelOne.getId().equals(subjectLevelTwo.getParentId())) {

                    //创建二级分类vo对象
                    SubjectVo subjectVoLevelTwo = new SubjectVo();
                    BeanUtils.copyProperties(subjectLevelTwo, subjectVoLevelTwo);
                    subjectVoLevelTwoList.add(subjectVoLevelTwo);
                }
            }
            subjectVolevelOne.setChildren(subjectVoLevelTwoList);
        }



        return subjectVoList;


    }
}
