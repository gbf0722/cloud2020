package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.vo.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseQueryVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author jack
 * @since 2019-12-26
 */
public interface CourseService extends IService<Course> {

    String saveCourseInfo(CourseInfoForm courseInfoForm);


    CourseInfoForm getCourseInfoFormById(String id);

    CoursePublishVo getCoursePublishVoById(String id);

    void publishCourseById(String id);

    Map<String, Object> webSelectPage(Page<Course> pageParam, WebCourseQueryVo webCourseQueryVo);

    WebCourseVo selectWebCourseVoById(String courseId);
}
