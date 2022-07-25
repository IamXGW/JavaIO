package com.iamxgw.file;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

public class ServerReaderThread extends Thread {
    private Socket socket;

    public ServerReaderThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String filePath = "/Users/xuguangwei/Documents/JavaProject/JavaIO/BIO/src/com/iamxgw/file/files/server/";
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String suffix = dataInputStream.readUTF();
            System.out.println("File suffix is : " + suffix);
            String fileName = filePath + UUID.randomUUID().toString() + suffix;
            OutputStream outputStream = new FileOutputStream(fileName);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = dataInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.close();
            System.out.println("Write file done.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
