package com.wjk.group.config;

import com.wjk.group.localCache.TransactionCache;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 注入上下文给后面使用
 */
@Configuration
@ComponentScan("com.wjk.group")
public class GTMConfig implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        TransactionCache.SPRING_CONTEXT = applicationContext;
    }
}
