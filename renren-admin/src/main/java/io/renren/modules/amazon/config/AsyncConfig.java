package io.renren.modules.amazon.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author zjr
 */
@SpringBootConfiguration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        // 设置ThreadPoolTaskExecutor的核心池大小
        threadPoolTaskExecutor.setCorePoolSize(10000);
        // 设置ThreadPoolTaskExecutor的最大池大小
        threadPoolTaskExecutor.setMaxPoolSize(150000);
        // 设置ThreadPoolTaskExecutor的BlockingQueue的容量
        threadPoolTaskExecutor.setQueueCapacity(200000);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
