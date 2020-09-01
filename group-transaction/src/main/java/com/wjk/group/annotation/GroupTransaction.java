package com.wjk.group.annotation;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式事务注解
 * 时间: 2020年8月23日15:03:30
 * 作者: 俊康
 * 已经集成了transaction,开分布式事务只需要这个注解，里面有当前事务
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Transactional
public @interface GroupTransaction {

    /**
     * 是否需要创建事务组
     * @return
     */
    boolean isCreate() default false;

}
