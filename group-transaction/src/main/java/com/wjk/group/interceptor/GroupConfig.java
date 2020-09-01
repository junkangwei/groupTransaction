package com.wjk.group.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: GTM-WJK
 * @description: 加入容器中
 * @author: junkang.wei
 * @create: 2020年08月24日14:06:37
 **/
@Configuration
public class GroupConfig implements WebMvcConfigurer {

    @Bean
    public GroupIdInterceptor groupIdInterceptor() {
        return new GroupIdInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor (groupIdInterceptor ()).addPathPatterns ("/**");
    }
}
