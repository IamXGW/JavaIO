package Client;

import util.Constants;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReader extends Thread {
    private Socket socket;
    private ClientChat clientChat ;

    public ClientReader(ClientChat clientChat, Socket socket) {
        this.clientChat = clientChat;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            while (true) {
                int flag = dataInputStream.readInt();
                if (flag == 1) {
                    String nameDatas = dataInputStream.readUTF();
                    String[] names = nameDatas.trim().split(Constants.SPILIT);
                    clientChat.onLineUsers.setListData(names);
                } else if (flag == 2) {
                    String msg = dataInputStream.readUTF();
                    clientChat.smsContent.append(msg);
                    clientChat.smsContent.setCaretPosition(clientChat.smsContent.getText().length());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
