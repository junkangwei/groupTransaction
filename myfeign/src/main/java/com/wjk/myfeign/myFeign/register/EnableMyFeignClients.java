package com.wjk.myfeign.myFeign.register;


import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否采用自己的FeignClient
 * 时间: 2020年10月20日16:03:49
 * 作者: 俊康
 * 自动注入
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MyFeignClientRegistrar.class)
public @interface EnableMyFeignClients {

    String[] value() default {};

    /**
     * 同mybatis,包扫描逻辑
     * @return
     */
    String[] basePackages() default {};
}
