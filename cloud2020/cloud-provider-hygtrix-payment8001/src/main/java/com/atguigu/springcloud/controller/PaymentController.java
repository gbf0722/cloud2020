package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.service.PaymentService;
import jdk.nashorn.internal.runtime.options.LoggingOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverport;

    @GetMapping("/payment/hystrix/ok/{id}")
    public String paymentInfo_ok(@PathVariable("id") Integer id) {
        String ok = paymentService.paymentInfo_ok(id);
        log.info("哈哈O(∩_∩)O" + ok);
        return ok;
    }

    @GetMapping("/payment/hystrix/timeout/{id}")
    public String paymentInfo_timeout(@PathVariable("id") Integer id) {
        String fail = paymentService.paymentInfo_timeout(id);
        log.info("哈哈O(∩_∩)O" + fail);
        return fail;
    }

    //服务熔断
    @GetMapping("/payment/circuit/{id}")
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        String result = paymentService.paymentCircuitBreaker(id);
        log.info(".......result:"+result);
        return result;




    }

}
