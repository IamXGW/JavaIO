package com.iamxgw.client;

import io.netty.handler.timeout.IdleStateHandler;

/**
 * @description: 连接写空闲检测
 * @author: IamXGW
 * @create: 2023-12-13 22:15
 */
public class CheckWriteIdleHandler extends IdleStateHandler {
    public CheckWriteIdleHandler() {
        super(0, 8, 0);
    }
}