package com.wjk.myfeign.myFeign.register;

import com.wjk.myfeign.myFeign.annotation.MyFeignClient;
import com.wjk.myfeign.myFeign.factoryBean.MyFeignClientFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * 扫描包
 */
public class MyFeignClientScanner extends ClassPathBeanDefinitionScanner {


    public MyFeignClientScanner(BeanDefinitionRegistry registry) {
        super(registry);
        registerFilters();
    }


    public void registerFilters() {
        // include all interfaces
        addIncludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                return true;
            }
        });


        // exclude package-info.java
        addExcludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                String className = metadataReader.getClassMetadata().getClassName();
                return className.endsWith("package-info");
            }
        });
    }


    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {

        Assert.notEmpty(basePackages, "At least one base package must be specified");
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            logger.warn("No feign plus client is found in package '" + Arrays.toString(basePackages) + "'.");
        }
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();

            definition = (ScannedGenericBeanDefinition) definition;
            Map<String, Object> annotationAttributes = ((ScannedGenericBeanDefinition) definition).getMetadata().getAnnotationAttributes(MyFeignClient.class.getName());
            //annotation.url();
            String url = (String) annotationAttributes.get("url");

            String beanClassName = definition.getBeanClassName();
            definition.setBeanClass(MyFeignClientFactoryBean.class);
            //设置proxyInterface属性，最后invoke的时候使用,mybatis其实还是设置了注入方式为2，我们这里不需要
            definition.getPropertyValues().add("proxyInterface", beanClassName);
            definition.getPropertyValues().add("url", url);
            //mybatis设置的，我们可以不设置
            //definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }

        return beanDefinitions;
    }

    private String buildUrl(Map<String, Object> myFeignClient, Map<String, Object> requestMapping) {


        return "1";
    }

    /**
     * 判断我们注入的是不是接口，可以业务上规定，也可以在写代码的是时候规定
     *
     * @param beanDefinition
     * @return
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface();
    }
}
