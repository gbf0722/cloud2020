package com.atguigu.guli.service.oss.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public interface FileService {

    /**
     * 文件上传至阿里云
     */
    String upload(InputStream inputStream, String module, String originalFilename);
}
