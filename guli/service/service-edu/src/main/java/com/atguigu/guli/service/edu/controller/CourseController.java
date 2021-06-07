package com.atguigu.guli.service.edu.controller;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.entity.vo.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.atguigu.guli.service.edu.service.CourseService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author jack
 * @since 2019-12-26
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/edu/course")
public class CourseController {

    @Autowired
    private CourseService courseService;



    @ApiOperation(value = "根据ID查询课程")
    @GetMapping("course-info/{id}")
    public R getById(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id){

        CourseInfoForm courseInfoForm = courseService.getCourseInfoFormById(id);
        return R.ok().data("item", courseInfoForm);
    }



}

