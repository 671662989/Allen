package com.leyou.goods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

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
@EnableFeignClients
@EnableDiscoveryClient
public class LeyouGoodsWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeyouGoodsWebApplication.class);
    }
}
