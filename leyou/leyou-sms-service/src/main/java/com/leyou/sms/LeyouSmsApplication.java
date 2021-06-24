package com.leyou.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

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
@EnableConfigurationProperties
public class LeyouSmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeyouSmsApplication.class,args);
    }
}
