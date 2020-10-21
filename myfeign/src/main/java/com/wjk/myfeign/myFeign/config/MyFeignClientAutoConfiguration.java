package com.wjk.myfeign.myFeign.config;


import feign.Client;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sun.net.www.http.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class MyFeignClientAutoConfiguration {

    private MyFeignClientProperties myFeignClientProperties;


    public MyFeignClientAutoConfiguration(MyFeignClientProperties myFeignClientProperties) {
        this.myFeignClientProperties = myFeignClientProperties;
    }

    @Bean
    public ConnectionPool connectionPool(){
        return new ConnectionPool(myFeignClientProperties.getMaxIdleConnections(),
                myFeignClientProperties.getKeepAliveDuration(), TimeUnit.MINUTES) ;
    }


    @Bean(value = "client")
    @ConditionalOnExpression("'okhttp3'.equals('${feign.httpclient:okhttp3}')")
    public Client okHttpClient(ConnectionPool connectionPool){
        OkHttpClient delegate = new OkHttpClient().newBuilder()
                .connectionPool(connectionPool)
                .connectTimeout(myFeignClientProperties.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(myFeignClientProperties.getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(myFeignClientProperties.getWriteTimeout(), TimeUnit.MILLISECONDS)
                .build();
        return new feign.okhttp.OkHttpClient(delegate) ;
    }

}
