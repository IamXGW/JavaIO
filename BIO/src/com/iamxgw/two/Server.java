package com.iamxgw.two;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *  目标：Server 和 Client 多发和多收
 */
public class Server {
    public static void main(String[] args) throws IOException {
        System.out.println("=== Server Start ===");
        ServerSocket ss = new ServerSocket(9999);
        Socket socket = ss.accept();
        InputStream is = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String msg;
        while ((msg = br.readLine()) != null) {
            System.out.println("Server 收到消息：" + msg);
        }
    }
}
