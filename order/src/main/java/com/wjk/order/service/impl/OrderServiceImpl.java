package com.wjk.order.service.impl;

import com.wjk.group.annotation.GroupTransaction;
import com.wjk.order.mapper.OrderMapper;
import com.wjk.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final RestTemplate restTemplate;

    /**
     * 开启分布式事务注解
     * isCreateTransaction = true说明是创建事务
     */
    @Override
    @GroupTransaction(isCreate = true)
    public void addOrder() {
        orderMapper.addOrder("name");
        System.out.println("我要发送远程服务了");
        System.out.println("当前时间开始是:" + System.currentTimeMillis());
        restTemplate.getForObject("http://127.0.0.1:8082/user/addUser",void.class);
        System.out.println("当前时间结束是:" + System.currentTimeMillis());
        System.out.println("我已经发送远程服务了");
    }
}
