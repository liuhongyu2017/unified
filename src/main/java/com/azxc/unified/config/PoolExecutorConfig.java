package com.azxc.unified.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 *
 * @author lhy
 * @version 1.0 2020/3/23
 */
@EnableAsync
@Configuration
public class PoolExecutorConfig {

  /**
   * 行为日志线程池
   */
  @Bean
  public AsyncTaskExecutor actionLogExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setThreadNamePrefix("action-log-1");
    executor.setMaxPoolSize(10);
    return executor;
  }
}
