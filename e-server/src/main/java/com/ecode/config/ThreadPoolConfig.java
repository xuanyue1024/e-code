package com.ecode.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;

/**
 * 线程池配置类
 * @author 竹林听雨
 */
@Configuration
@Slf4j
public class ThreadPoolConfig {

    @Bean
    public ExecutorService taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int core = Runtime.getRuntime().availableProcessors();

        executor.setCorePoolSize(core);           // 核心线程数
        executor.setMaxPoolSize(core * 2);            // 最大线程数
        executor.setKeepAliveSeconds(30);      // 空闲线程存活时间（秒）
        executor.setQueueCapacity(100);        // 任务队列容量
//        executor.setThreadNamePrefix("ecode-Async-"); // 线程名前缀
        executor.setRejectedExecutionHandler((task, executor1) -> {
            // 记录任务信息或发送告警
            log.error("任务被拒绝：线程池已满，队列容量不足！Task: {}", task);
        });
        executor.setAllowCoreThreadTimeOut(true); // 允许核心线程超时回收（避免长期闲置浪费资源）
//        executor.setThreadFactory(factory);

        executor.initialize();
        return executor.getThreadPoolExecutor();
    }
}