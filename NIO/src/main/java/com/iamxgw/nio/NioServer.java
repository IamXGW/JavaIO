package com.iamxgw.nio;

import com.iamxgw.Const;

public class NioServer {
    private static NioServerHandle nioServerHandle;

    public static void main(String[] args) {
        nioServerHandle = new NioServerHandle(Const.DEFAULT_PORT);
        new Thread(nioServerHandle, "Server").start();
    }
}
