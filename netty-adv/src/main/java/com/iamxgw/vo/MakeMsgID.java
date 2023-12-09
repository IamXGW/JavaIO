package com.iamxgw.vo;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @description:   消息 ID 生成器
 * @author: IamXGW
 * @create: 2023-12-09 15:42
 */
public class MakeMsgID {
    private static AtomicLong msgId = new AtomicLong(1);

    public static long getId() {
        return msgId.getAndIncrement();
    }
}