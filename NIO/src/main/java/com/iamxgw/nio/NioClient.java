package com.iamxgw.nio;

import com.iamxgw.Const;

import java.util.Scanner;

public class NioClient {
    private static NioClientHandle nioClientHandle;

    public static void main(String[] args) throws Exception {
        start();
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            sendMsg(sc.next());
        }
    }

    private static void start() {
        nioClientHandle = new NioClientHandle(Const.DEFAULT_SERVER_IP, Const.DEFAULT_PORT);
        new Thread(nioClientHandle, "Client").start();
    }

    private static boolean sendMsg(String msg) throws Exception {
        nioClientHandle.sendMsg(msg);
        return true;
    }
}
