package org.ping_me;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PingMeUtilityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PingMeUtilityServiceApplication.class, args);
    }

}
