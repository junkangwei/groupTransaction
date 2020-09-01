package com.wjk.server;


/**
 * @program: GTM-WJK
 * @description: netty server启动类
 * @author: junkang.wei
 * @create: 2020-08-23 15:36
 **/
public class ServerMain {

    /**
     * 端口号
     */
    public static final Integer PORT = 8888;

    public static void main(String[] args) throws Exception {
        // 启动
        new TrmServer ().bind (PORT);
    }



}
