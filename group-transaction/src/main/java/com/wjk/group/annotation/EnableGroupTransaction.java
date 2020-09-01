package com.wjk.group.annotation;

import com.wjk.group.config.GTMConfig;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 集成分布式事务注解
 * 时间: 2020年09月01日18:06:34
 * 作者: 俊康
 * 自动注入
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * spring扫描使用
 */
@Import(GTMConfig.class)
public @interface EnableGroupTransaction {

}
