package com.iamxgw.four;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HandlerSocketServerPool {
    private ExecutorService executorService;
    public HandlerSocketServerPool(int maxThreadsNum, int queueSize) {
        executorService = new ThreadPoolExecutor(3, maxThreadsNum, 120,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueSize));
    }
    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }
}
