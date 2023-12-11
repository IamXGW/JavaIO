package com.iamxgw.server.asyncpro;

import io.netty.util.NettyRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @description: 异步消息处理器
 * @author: IamXGW
 * @create: 2023-12-11 22:30
 */
public class AsyncBusiProcess {
    private static final Logger LOG = LoggerFactory.getLogger(AsyncBusiProcess.class);

    private static BlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<>(3000);

    private static ExecutorService executorService
            = new ThreadPoolExecutor(1, NettyRuntime.availableProcessors(),
            60, TimeUnit.SECONDS, taskQueue);

    public static void submitTask(Runnable task) {
        executorService.submit(task);
    }
}