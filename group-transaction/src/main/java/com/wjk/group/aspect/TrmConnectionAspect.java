package com.wjk.group.aspect;

import com.wjk.group.connection.TmConnection;
import com.wjk.group.netty.NettyClient;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;

/**
 * @program: GTM-WJK
 * @description: 事务类，当启动的时候，就加入到spring容器当中
 * @author: junkang.wei
 * @create: 2020-08-23 15:10
 **/
@Component
@Aspect
public class TrmConnectionAspect {

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection connection(ProceedingJoinPoint pjp) throws Throwable {
        Connection oldConnection = (Connection) pjp.proceed ();
        System.out.println("获得连接" + oldConnection);
        return new TmConnection(oldConnection);
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        //创建一个事务
        NettyClient client = new NettyClient("localhost", 8888);
        client.start ();
        System.out.println("客户端连接成功");
        NettyClient.client = client;
    }
}
