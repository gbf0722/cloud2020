package com.atguigu.guli.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.vod.service.VideoService;
import com.atguigu.guli.service.vod.util.AliyunVodSDKUtils;
import com.atguigu.guli.service.vod.util.VodProperties;
import com.sun.media.jfxmedia.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VodProperties vodProperties;

    @Override
    public String uploadVideo(InputStream inputStream, String originalFilename) {
        String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));

        UploadStreamRequest request = new UploadStreamRequest(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret(),
                title, originalFilename, inputStream
        );

        UploadVideoImpl uploadVideo = new UploadVideoImpl();
        UploadStreamResponse response = uploadVideo.uploadStream(request);

        String videoId = response.getVideoId();
        if (StringUtils.isEmpty(videoId)) {
            log.error("阿里云上传失败"+response.getCode()+"-"+response.getMessage());
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }
        return videoId;
    }

    @Override
    public void removeVideo(String vodId) throws ClientException {
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret());
        DeleteVideoRequest request = new DeleteVideoRequest();
        request.setVideoIds(vodId);
        client.getAcsResponse(request);

    }

    @Override
    public Map<String, Object> getVideoUploadAuthAndAddress(String title, String fileName) throws ClientException {
        //初始化client对象
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret());

        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        request.setTitle(title);
        request.setFileName(fileName);

        //获取相应
        CreateUploadVideoResponse response = client.getAcsResponse(request);

        HashMap<String,Object> map = new HashMap<>();
        map.put("videoId", response.getVideoId());
        map.put("uploadAddress", response.getUploadAddress());
        map.put("uploadAuth", response.getUploadAuth());

        return map;
    }

    @Override
    public Map<String, Object> refreshVideoUploadAuth(String videoId) throws ClientException {
        //初始化client对象
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret());

        //创建请求对象
        RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
        request.setVideoId(videoId);

        //获取响应
        RefreshUploadVideoResponse response = client.getAcsResponse(request);

        Map<String, Object> map = new HashMap<>();
        map.put("videoId", response.getVideoId());
        map.put("uploadAddress", response.getUploadAddress());
        map.put("uploadAuth", response.getUploadAuth());

        return map;



    }

    @Override
    public String getVideoPlayAuth(String videoSourceId) throws ClientException {
        //初始化client对象
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret());

        //创建请求对象
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoSourceId);

        //获取响应
        GetVideoPlayAuthResponse response= client.getAcsResponse(request);

        return response.getPlayAuth();
    }
}
