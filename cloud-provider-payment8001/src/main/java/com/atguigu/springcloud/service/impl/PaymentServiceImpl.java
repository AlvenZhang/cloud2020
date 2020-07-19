package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.dao.PaymentDao;
import com.atguigu.springcloud.entity.Payment;
import com.atguigu.springcloud.service.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private PaymentDao paymentDao;

    @Override
    public int create(Payment payment) {

        int result = paymentDao.create(payment);

        return result;
    }

    @Override
    public Payment getPaymentById(long id) {

        Payment payment = paymentDao.getPaymentById(id);

        return payment;
    }
}
