package com.iamxgw.server.asyncpro;

import com.iamxgw.vo.MyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 默认任务处理器
 * @author: IamXGW
 * @create: 2023-12-11 21:36
 */
public class DefaultTaskProcessor implements ITaskProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultTaskProcessor.class);

    @Override
    public Runnable execAsyncTask(MyMessage msg) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                LOG.info("DefaultTaskProcessor 模拟任务处理：" + msg.getBody());
            }
        };
        return task;
    }
}