package com.leyou.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * create by: cdy
 * description:$
 * create time: $ $
 * <p>
 * $
 *
 * @return $
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties
@EnableFeignClients
public class LeyouAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeyouAuthServiceApplication.class, args);
    }
}
