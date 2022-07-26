import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    public static Set<Socket> allClinetOnLine = new HashSet<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            while (true) {
                Socket socket = serverSocket.accept();
                allClinetOnLine.add(socket);
                new BufferReaderThread(socket).start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
