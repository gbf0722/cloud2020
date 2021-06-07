package com.atguigu.mybatis_plus.hander;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {

        log.info("start insert fill ....");
        Date now = new Date();
        this.setFieldValByName("createTime", now, metaObject);
        this.setFieldValByName("updateTime", now, metaObject);


    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        Date now = new Date();
        this.setFieldValByName("updateTime", now, metaObject);
    }
}

