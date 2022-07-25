package com.iamxgw.file;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        String fileName = "/Users/xuguangwei/Documents/JavaProject/JavaIO/BIO/src/com/iamxgw/file/files/IMG_1705.JPG";
        try (
                InputStream inputStream = new FileInputStream(fileName);
                ) {
            Socket socket = new Socket("127.0.0.1", 8888);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            String suffix = "." + fileName.split("\\.")[1];
            dataOutputStream.writeUTF(suffix);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                dataOutputStream.write(buffer, 0, len);
            }
            dataOutputStream.flush();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
