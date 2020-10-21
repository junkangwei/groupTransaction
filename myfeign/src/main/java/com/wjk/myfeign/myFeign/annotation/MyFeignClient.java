package com.wjk.myfeign.myFeign.annotation;

import java.lang.annotation.*;

/**
 * @program: GTM-WJK
 * @description: 自己的feign客户端
 * @author: junkang.wei
 * @create: 2020-10-20 15:59
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyFeignClient {
    /**
     * 服务名称
     * @return
     */
    String name() default "";

    /**
     * url
     * @return
     */
    String url() default "";
}
