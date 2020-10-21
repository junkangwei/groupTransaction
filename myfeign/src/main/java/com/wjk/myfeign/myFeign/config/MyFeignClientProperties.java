package com.wjk.myfeign.myFeign.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * feign客户端设置
 */
@Configuration
public class MyFeignClientProperties {


    private int maxIdleConnections = 200;

    /**
     * The default is 5 minutes.
     */
    private long keepAliveDuration = 5;

    /** Default connection timeout (in milliseconds). The default is 10 seconds. */
    private int connectTimeout = 10 * 1000;
    private int readTimeout = 10 * 1000;
    private int writeTimeout = 10 * 1000;


    public int getMaxIdleConnections() {
        return maxIdleConnections;
    }

    public void setMaxIdleConnections(int maxIdleConnections) {
        this.maxIdleConnections = maxIdleConnections;
    }

    public long getKeepAliveDuration() {
        return keepAliveDuration;
    }

    public void setKeepAliveDuration(long keepAliveDuration) {
        this.keepAliveDuration = keepAliveDuration;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }
}
