package com.wjk.myfeign.myFeign.register;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类似spring-整合Mybatis
 * 注册文件MapperScannerRegistrar
 */
public class MyFeignClientRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {

    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableMyFeignClients.class.getName()));

        if(annotationAttributes != null){
            //扫描包，注册到spring容器当中
            registerBeanDefinitions(annotationAttributes,beanDefinitionRegistry);
        }

    }

    private void registerBeanDefinitions(AnnotationAttributes annotationAttributes,BeanDefinitionRegistry registry) {
        List<String> basePackages = new ArrayList<>();
        //获得需要扫描的路径
        if(annotationAttributes != null){
            basePackages.addAll(Arrays.stream(annotationAttributes.getStringArray("basePackages")).filter(StringUtils::hasText)
                    .collect(Collectors.toList()));
        }
        //扫描包的逻辑
        MyFeignClientScanner myFeignClientScanner = new MyFeignClientScanner(registry);
        myFeignClientScanner.doScan(StringUtils.toStringArray(basePackages));

    }
}
