package com.leyou.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * create by: cdy
 * description:$
 * create time: $ $
 * <p>
 * $
 *
 * @return $
 */
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.leyou.user.mapper")
public class LeyouUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeyouUserServiceApplication.class,args);
    }
}
