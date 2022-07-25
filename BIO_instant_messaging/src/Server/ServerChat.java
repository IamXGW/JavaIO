package Server;

import util.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerChat {
    public static Map<Socket, String> onLineSockets = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(Constants.PORT);
        while (true) {
            Socket socket = serverSocket.accept();
            new ServerReader(socket).start();
        }
    }
}
