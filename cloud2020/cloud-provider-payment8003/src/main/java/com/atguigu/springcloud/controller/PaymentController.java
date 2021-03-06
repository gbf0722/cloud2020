package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.TIMEOUT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverport;

    @Resource
    private DiscoveryClient discoveryClient;




    @PostMapping(value="/payment/create")
    public CommonResult<Payment> create(@RequestBody Payment payment) {
        int result = paymentService.create(payment);
        log.info("-----插入结果是"+result);
        //System.out.println("======================================="+payment);


        if (result > 0) {
            return new CommonResult(200, "插入数据库成功,端口号是"+serverport, result);
        } else {
            return new CommonResult(444,"插入数据失败,端口号是"+serverport,null);
        }
    }

    @GetMapping(value="/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getPaymentById(id);
        log.info("-----查询结果是"+payment);

        if (payment != null) {
            return new CommonResult<Payment>(200, "查询成功,端口号是"+serverport, payment);
        } else {
            return new CommonResult<Payment>(444, "没有查询到数据，查询id为"+id+"端口号是"
                    +serverport, null);
        }
    }

    @GetMapping("/payment/discovery")
    public Object  discovery() {
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            log.info("**********element :"+element);
        }
/*        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PROVIDER-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getInstanceId()+"\t"+instance.getHost()+"\t"+
                    instance.getPort()+"\t"+instance.getUri());
        }*/
        return this.discoveryClient;
    }


    @GetMapping(value="/payment/feign/timeout")
    public String paymentFeignTimeout() {
        //业务逻辑正确，但是需要耗时三秒钟
        try {
            TimeUnit.SECONDS.sleep(3);

        } catch (InterruptedException e) {
            e.printStackTrace();

        }
        return serverport;

    }


    @GetMapping(value="/payment/lb/{id}")
    public String loadblance(@PathVariable("id") Long id) {

        return serverport;

    }

}
