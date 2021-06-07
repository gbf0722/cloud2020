package com.atguigu.mybatis_plus;

import com.atguigu.mybatis_plus.entity.User;
import com.atguigu.mybatis_plus.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisPlusApplicationTests {


    @Autowired
    private UserMapper userMapper;

    @Test
    public void contextLoads() {
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);


    }

}
