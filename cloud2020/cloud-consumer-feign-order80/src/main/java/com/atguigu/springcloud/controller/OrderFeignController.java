package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentFeignService;
import com.atguigu.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@Slf4j
@RestController
@DefaultProperties(defaultFallback = "paymentTimeOutFallbackMethod")
public class OrderFeignController {

    @Resource
    private PaymentFeignService paymentFeignService;

    @Resource
    private PaymentHystrixService paymentHystrixService;

    @GetMapping(value = "/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id)
    {
        return paymentFeignService.getPaymentById(id);
    }


    @GetMapping(value="/consumer/payment/feign/timeout")
    @HystrixCommand
    public String paymentFeignTimeout() {
        return  paymentFeignService.paymentFeignTimeout();
    }

    @GetMapping("/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_ok(@PathVariable("id") Integer id) {
         return paymentHystrixService.paymentInfo_ok(id);
    }


    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
    public String paymentInfo_timeout(@PathVariable("id") Integer id) {
       return  paymentHystrixService.paymentInfo_timeout(id);
    }


    public String paymentTimeOutFallbackMethod() {
        return "global我是消费方80,对方支付系统繁华，请稍微再试，苦苦/(ㄒoㄒ)/~~ ";
    }

}
