package com.lyq.yuqiapigateway;

import com.lyq.apiProject.provider.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableDubbo
@Service
public class YuqiApiGatewayApplication {
    @DubboReference
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(YuqiApiGatewayApplication.class, args);
        YuqiApiGatewayApplication application = context.getBean(YuqiApiGatewayApplication.class);
        String result = application.doSayHello("world");
        String result2 = application.doSayHello2("world2");
        System.out.println("result: "+result);
        System.out.println("result2: "+result2);
    }

    public String doSayHello(String name) {
        return demoService.sayHello(name);
    }

    public String doSayHello2(String name) {
        return demoService.sayHello2(name);
    }

}
