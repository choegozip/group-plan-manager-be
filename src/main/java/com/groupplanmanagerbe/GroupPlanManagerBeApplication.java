package com.groupplanmanagerbe;

import com.groupplanmanagerbe.global.security.model.JwtSecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableRetry
@EnableConfigurationProperties(JwtSecurityProperties.class)
public class GroupPlanManagerBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(GroupPlanManagerBeApplication.class, args);
    }
}
