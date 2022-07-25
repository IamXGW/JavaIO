package Server;

import util.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Set;

public class ServerReader extends Thread {
    private Socket socket;

    public ServerReader(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        DataInputStream dataInputStream = null;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            while (true) {
                int flag = dataInputStream.readInt();
                if (flag == 1) {
                    String loginName = dataInputStream.readUTF();
                    System.out.println(loginName + "---->" + socket.getRemoteSocketAddress());
                    ServerChat.onLineSockets.put(socket, loginName);
                }
                writeMsg(flag, dataInputStream);
            }
        } catch (IOException e) {
            System.out.println("--有人下线了--");
            ServerChat.onLineSockets.remove(socket);
            try {
                writeMsg(1, dataInputStream);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void writeMsg(int flag, DataInputStream dataInputStream) throws IOException {
        String msg = null;
        if (flag == 1) {
            StringBuilder sb = new StringBuilder();
            Collection<String> names = ServerChat.onLineSockets.values();
            for (String name : names) {
                sb.append(name);
                sb.append(Constants.SPILIT);
            }
            msg = sb.toString().trim().substring(0, sb.lastIndexOf(Constants.SPILIT));
            sendMsgToAll(flag, msg);
        } else if (flag == 2 || flag == 3) {
            String newMsg = dataInputStream.readUTF();
            String sendName = ServerChat.onLineSockets.get(socket);
            StringBuilder sb = new StringBuilder();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EEE");
            if (flag == 2) {
                sb.append(sendName).append("  ").append(sdf.format(System.currentTimeMillis() * 2)).append("\r\n");
                sb.append("    ").append(newMsg).append("\r\n");
                msg = sb.toString();
                sendMsgToAll(2, msg);
            } else {
                sb.append(sendName).append("  ").append(sdf.format(System.currentTimeMillis() * 2)).append(" 对您私发\r\n");
                sb.append("    ").append(newMsg).append("\r\n");
                String destName = dataInputStream.readUTF().trim();
                msg = sb.toString();
                sendMsgToOne(destName, msg);
            }
        }
    }

    private void sendMsgToOne(String destName, String msg) throws IOException {
        Set<Socket> allOnLineSockets = ServerChat.onLineSockets.keySet();
        for (Socket sk : allOnLineSockets) {
            if (ServerChat.onLineSockets.get(sk).equals(destName)) {
                DataOutputStream dataOutputStream = new DataOutputStream(sk.getOutputStream());
                dataOutputStream.writeInt(2);
                dataOutputStream.writeUTF(msg);
                dataOutputStream.flush();
                break;
            }
        }
    }

    private void sendMsgToAll(int flag, String msg) throws IOException {
        Set<Socket> allOnLineSockets = ServerChat.onLineSockets.keySet();
        for (Socket sk : allOnLineSockets) {
            DataOutputStream dataOutputStream = new DataOutputStream(sk.getOutputStream());
            dataOutputStream.writeInt(flag);
            dataOutputStream.writeUTF(msg);
            dataOutputStream.flush();
        }
    }
}
