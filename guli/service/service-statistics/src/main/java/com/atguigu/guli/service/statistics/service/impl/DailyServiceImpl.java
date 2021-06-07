package com.atguigu.guli.service.statistics.service.impl;

import com.atguigu.guli.common.base.result.R;

import com.atguigu.guli.service.statistics.client.UcenterClient;
import com.atguigu.guli.service.statistics.entity.Daily;
import com.atguigu.guli.service.statistics.mapper.DailyMapper;
import com.atguigu.guli.service.statistics.service.DailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.apache.velocity.runtime.directive.contrib.For;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author jack
 * @since 2020-01-11
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UcenterClient ucenterClient;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createStatisticsByDay(String day) {

        //如果当日的统计结果记录已经存在，则删除重新统计或提示用户当日记录已经存在
        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_calculated", day);
        baseMapper.delete(queryWrapper);

        //生成统计记录
        //R r = restTemplate.getForObject("http://guli-ucenter/admin/ucenter/member/count-register/{day}", R.class, day);
        R r = ucenterClient.registerCount(day);
        Integer registerNum = (Integer) r.getData().get("registerCount");
        Integer loginNum = RandomUtils.nextInt(100, 200); //TODO
        Integer videoViewNum = RandomUtils.nextInt(100, 200); //TODO
        Integer courseNum = RandomUtils.nextInt(100, 200); //TODO

        Daily daily = new Daily();
        daily.setRegisterNum(registerNum);
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        daily.setDateCalculated(day);

        baseMapper.insert(daily);


    }

    @Override
    public Map<String, Object> getChartData(String begin, String end, String type) {
        System.out.println(type);
        Map<String, Object> map = new HashMap<>();

        ArrayList<String> xList = new ArrayList<>(); //日期列表
        ArrayList<Integer> yList = new ArrayList<>(); //数据列表

        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(type, "date_calculated");
        queryWrapper.between("date_calculated", begin, end);

        List<Map<String, Object>> mapsData = baseMapper.selectMaps(queryWrapper);

        System.out.println(mapsData);
        for (Map<String, Object> data : mapsData) {

            String dataCalculated = (String) data.get("date_calculated");
            xList.add(dataCalculated);

            Integer count = (Integer) data.get(type);
            yList.add(count);
        }


        map.put("xList", xList);
        map.put("yList", yList);

        System.out.println(map);

        return map;
    }
}
