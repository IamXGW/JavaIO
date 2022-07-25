package com.iamxgw.three;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        while (true) {
            Socket socket = serverSocket.accept();
            ServerThreadReader serverThreadReader = new ServerThreadReader(socket);
            serverThreadReader.start();
        }
    }
}
