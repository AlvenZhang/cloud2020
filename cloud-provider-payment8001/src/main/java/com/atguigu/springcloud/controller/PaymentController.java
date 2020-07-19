package com.atguigu.springcloud.controller;


import com.atguigu.springcloud.entity.Payment;
import com.atguigu.springcloud.service.PaymentService;
import com.atguigu.springcloud.utils.CommonResult;
import lombok.extern.slf4j.Slf4j;
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
    private String serverPort;



    @PostMapping("/payment/create")
    public CommonResult<Payment> create(@RequestBody Payment payment){

        log.info(payment.toString());

        int result = paymentService.create(payment);

        log.info("*****插入结果："+result);

        Integer code;
        String message;

        if (result > 0){
            code = 200;
            message = "插入数据库成功," + serverPort;

            return new CommonResult<Payment>(code,message);
        }else {
            code = 444;
            message = "插入数据库失败";

            return new CommonResult<>(code,message);
        }

    }

    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){

        Payment payment = paymentService.getPaymentById(id);

        log.info("*****查询结果：" + payment);

        Integer code;
        String message;

        if (payment != null){
            code = 200;
            message = "查询数据库成功," + serverPort;

            return new CommonResult<>(code,message,payment);
        }else {
            code = 444;
            message = "查询数据库失败";

            return new CommonResult<>(code,message,null);
        }

    }

    @Resource
    private DiscoveryClient discoveryClient;


    @GetMapping(value = "/payment/discovery")
    public Object discovery(){
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            log.info("***** element:"+element);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
        }
        return this.discoveryClient;
    }

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB(){
        return serverPort;
    }


    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout(){
        try { TimeUnit.SECONDS.sleep(3); }catch (Exception e) {e.printStackTrace();}
        return serverPort;
    }
}