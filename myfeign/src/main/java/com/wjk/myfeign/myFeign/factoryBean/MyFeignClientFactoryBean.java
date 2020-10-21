package com.wjk.myfeign.myFeign.factoryBean;

import com.wjk.myfeign.myFeign.config.MyFeignClientProperties;
import feign.Client;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.TimeUnit;

/**
 * @program: GTM-WJK
 * @description: 类似于spring整合mybatis, setBeanClass(MyFeignClientFactoryBean.class)
 * @author: junkang.wei
 * @create: 2020-10-20 15:59
 **/
public class MyFeignClientFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {


    private ApplicationContext applicationContext;

    private Class<T> proxyInterface ;

    private String url ;

    @Override
    public T getObject() throws Exception {
        //返回代理对象
        MyFeignClientProperties conf = applicationContext.getBean(MyFeignClientProperties.class) ;
        Client client ;
        try {
            client = applicationContext.getBean("client", Client.class) ;
        }catch (NoSuchBeanDefinitionException e){
            throw new NullPointerException("Without one of [okhttp3, Http2Client] client.") ;
        }

        return Feign.builder()
                .client(client)
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .retryer(new Retryer.Default(100, TimeUnit.SECONDS.toMillis(1L), 0))
                .options(new Request.Options(conf.getConnectTimeout(),conf.getReadTimeout(), true))
                .target(proxyInterface, url);
    }

    @Override
    public Class<?> getObjectType() {
        return proxyInterface;
    }


    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //设置spring上下文
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public Class<T> getProxyInterface() {
        return proxyInterface;
    }

    public void setProxyInterface(Class<T> proxyInterface) {
        this.proxyInterface = proxyInterface;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
