package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.entity.form.VideoInfoForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author jack
 * @since 2019-12-26
 */
public interface VideoService extends IService<Video> {

    void saveVideoInfo(VideoInfoForm videoInfoForm);

    void updateVideoInfoById(VideoInfoForm videoInfoForm);

    void removeVideoById(String id);

    VideoInfoForm getVideoInfoFormById(String id);
}
