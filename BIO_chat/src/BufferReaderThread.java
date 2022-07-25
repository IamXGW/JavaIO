import java.io.*;
import java.net.Socket;

public class BufferReaderThread extends Thread {
    private Socket socket;

    public BufferReaderThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg;
            while ((msg = bufferedReader.readLine()) != null) {
                sendMagToAllClient(msg);
            }
        } catch (Exception e) {
            
        }
    }

    private void sendMagToAllClient(String msg) {
        try {
            for (Socket sk : Server.allClinetOnLine) {
                PrintStream printStream = new PrintStream(sk.getOutputStream());
                printStream.println(msg);
                printStream.flush();
            }
        } catch (IOException e) {
            Server.allClinetOnLine.remove(socket);
        }
    }
}
