package com.atguigu.mybatis_plus;

import com.atguigu.mybatis_plus.entity.User;
import com.atguigu.mybatis_plus.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CRUDTests {

    @Autowired
    private UserMapper userMapper;


    @Test
    public void test01() {
        User user = new User();
        user.setName("Helen");
        user.setAge(18);
        user.setEmail("55317332@qq.com");

        int result = userMapper.insert(user);
        log.info("影响的行数：" + result); //影响的行数
        log.info("id：" + user); //id自动回填
    }

    @Test
    public void testUpdateById(){

        User user = new User();
        user.setId(1209464813357420548L);
        user.setAge(28);

        int result = userMapper.updateById(user);
        log.info("影响的行数：" + result);
    }


    @Test
    public void testConcurrentUpdate() {

        //1、查询数据
        User user1 = userMapper.selectById(1L);
        //2、修改数据
        user1.setViewCount(user1.getViewCount() + 1);

        //模拟另一个用户中间也进行了数据查询和更新操作
        User user2 = userMapper.selectById(1L);
        user2.setViewCount(user2.getViewCount() + 1);
        int result2 = userMapper.updateById(user2);
        log.info(result2 > 0 ? "user2更新成功" : "user2更新失败");

        //3、执行更新
        int result1 = userMapper.updateById(user1);
        log.info(result1 > 0 ? "user1更新成功" : "user1更新失败");
    }


    @Test
    public void testSelectBatchIds(){

        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        users.forEach(System.out::println);
    }

    @Test
    public void testSelectByMap(){

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Helen");
        map.put("age", 18);
        List<User> users = userMapper.selectByMap(map);

        users.forEach(System.out::println);
    }


    @Test
    public void testSelectPage() {

        Page<User> page = new Page<>(1,5);
        userMapper.selectPage(page, null);

        page.getRecords().forEach(System.out::println);
        System.out.println(page.getCurrent());
        System.out.println(page.getPages());
        System.out.println(page.getSize());
        System.out.println(page.getTotal());
        System.out.println(page.hasNext());
        System.out.println(page.hasPrevious());
    }


    /**
     * 当指定了特定的查询列时，希望分页结果列表只返回被查询的列，而不是很多null值
     * 可以使用selectMapsPage返回Map集合列表
     */
   @Test
    public void testSelectMapsPage() {

        Page<User> page = new Page<>(1, 5);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name", "age");

        IPage<Map<String, Object>> mapIPage = userMapper.selectMapsPage(page, queryWrapper);

        //注意：此行必须使用 mapIPage 获取记录列表，否则会有数据类型转换错误
        List<Map<String, Object>> records = mapIPage.getRecords();
        records.forEach(System.out::println);

        System.out.println(mapIPage.getCurrent());
        System.out.println(mapIPage.getPages());
        System.out.println(mapIPage.getSize());
        System.out.println(mapIPage.getTotal());
        //        System.out.println(mapIPage.hasNext());
        //        System.out.println(mapIPage.hasPrevious());
        System.out.println(page.hasNext());
        System.out.println(page.hasPrevious());
    }


    /**
     * 测试 逻辑删除
     */
    @Test
    public void testLogicDelete() {

        int result = userMapper.deleteById(1L);
        System.out.println(result);
    }


    /**
     * 测试 逻辑删除后的查询：
     * 不包括被逻辑删除的记录
     */
    @Test
    public void testLogicDeleteSelect() {

        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }



    /**
     * 测试 性能分析插件
     */
    @Test
    public void testPerformance() {
        User user = new User();
        user.setName("我是Helen");
        user.setEmail("helen@sina.com");
        user.setAge(18);
        userMapper.insert(user);
    }



}
