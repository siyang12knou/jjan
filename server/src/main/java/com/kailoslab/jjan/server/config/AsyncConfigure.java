package com.kailoslab.jjan.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Configuration
@EnableAsync
public class AsyncConfigure {

    private final int CORE_POOL_SIZE = 3;
    private final int MAX_POOL_SIZE = 20;
    private final int QUEUE_CAPACITY = 15;

    @Bean(name = "threadPoolExecutorService")
    public ExecutorService threadPoolTaskExecutor() {
        ExecutorService executorService =
                new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(QUEUE_CAPACITY));
        return executorService;
    }

}

