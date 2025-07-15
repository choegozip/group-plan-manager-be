package com.groupplanmanagerbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GroupPlanManagerBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(GroupPlanManagerBeApplication.class, args);
    }
}
