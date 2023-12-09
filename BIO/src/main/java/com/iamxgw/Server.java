package com.iamxgw;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static ExecutorService executorService
            = Executors.newFixedThreadPool(
                    Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(10001));
        System.out.println("Start Server...");
        try {
            while (true) {
//                new Thread(new ServerTask(serverSocket.accept())).start();
                executorService.execute(new ServerTask(serverSocket.accept()));
            }
        } finally {
            serverSocket.close();
        }
    }

    private static class ServerTask implements Runnable {
        private Socket socket = null;

        public ServerTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream inputStream =
                         new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream outputStream =
                         new ObjectOutputStream(socket.getOutputStream())) {
                String name = inputStream.readUTF();
                System.out.println("Server accept client msg: " + name);
                outputStream.writeUTF("hello, " + name);
                outputStream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
