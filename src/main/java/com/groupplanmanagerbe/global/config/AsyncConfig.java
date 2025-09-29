package com.groupplanmanagerbe.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 기본 실행 스레드 개수
        executor.setMaxPoolSize(20); // 최대 스레드 개수
        executor.setQueueCapacity(200); // 대기열 크기
        executor.setThreadNamePrefix("Async-FCM-");
        executor.initialize();
        return executor;
    }
}
