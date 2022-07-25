package com.iamxgw.four;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        HandlerSocketServerPool pool = new HandlerSocketServerPool(3, 10);
        while (true) {
            Socket socket = serverSocket.accept();
            Runnable target = new ServerRunnableTarget(socket);
            pool.execute(target);
        }
    }
}
