package com.wjk.user.service.impl;

import com.wjk.group.annotation.GroupTransaction;
import com.wjk.user.mapper.UserMapper;
import com.wjk.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;


    /**
     * 开启分布式事务注解
     * isCreateTransaction = true说明是创建事务
     */
    @Override
    @GroupTransaction()
    public void addUser() {
        System.out.println("当前时间开始是:" + System.currentTimeMillis());
        userMapper.addUser(UUID.randomUUID().toString());
        System.out.println("当前时间结束是:" + System.currentTimeMillis());

    }
}
