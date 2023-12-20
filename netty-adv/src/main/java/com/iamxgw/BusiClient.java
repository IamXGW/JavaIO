package com.iamxgw;

/**
 * @description: 业务方如何调用Netty客户端演示
 * @author: IamXGW
 * @create: 2023-12-15 15:04
 */
public class BusiClient {
    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        new Thread().start();
    }
}