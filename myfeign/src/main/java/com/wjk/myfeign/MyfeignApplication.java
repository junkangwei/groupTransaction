package com.wjk.myfeign;

import com.wjk.myfeign.myFeign.FeignClientUse.UserFeign;
import com.wjk.myfeign.myFeign.register.EnableMyFeignClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableMyFeignClients(basePackages = "com.wjk.myfeign.myFeign.FeignClientUse")
@RestController
public class MyfeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyfeignApplication.class, args);
    }

    @Autowired
    private UserFeign userFeign;

    @RequestMapping("/test1")
    public String addOrder() {
        userFeign.addOrder();
        return "1";
    }
}
