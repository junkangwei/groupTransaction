package com.wjk.myfeign.myFeign.FeignClientUse;

import com.wjk.myfeign.myFeign.annotation.MyFeignClient;
import feign.RequestLine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@MyFeignClient(url = "127.0.0.1:8899")
public interface UserFeign {

    @GetMapping(value = "/order/addOrder")
    Void addOrder();
}
