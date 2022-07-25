package Client;

import util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientChat implements ActionListener {
    private JFrame win = new JFrame();
    public JTextArea smsContent = new JTextArea(23, 50);
    private JTextArea smsSend = new JTextArea(4, 40);
    public JList<String> onLineUsers = new JList<>();
    private JCheckBox isPrivateBn = new JCheckBox("私聊");
    private JButton sendBn = new JButton("发送");
    private JFrame loginView;
    private JTextField ipEt, nameEt, idEt;
    private Socket socket;

    public static void main(String[] args) {
        new ClientChat().initView();
    }

    private void initView() {
        win.setSize(650, 600);
        displayLoginView();
    }

    private void displayChatView() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        win.add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(smsSend);
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btns.add(sendBn);
        btns.add(isPrivateBn);
        bottomPanel.add(btns, BorderLayout.EAST);
        smsContent.setBackground(new Color(0xdd, 0xdd, 0xdd));
        win.add(new JScrollPane(smsContent), BorderLayout.CENTER);
        smsContent.setEditable(false);
        Box rightBox = new Box(BoxLayout.Y_AXIS);
        onLineUsers.setFixedCellWidth(120);
        onLineUsers.setVisibleRowCount(13);
        rightBox.add(new JScrollPane(onLineUsers));
        win.add(rightBox, BorderLayout.EAST);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.pack();
        setWindowCenter(win, 650, 600, true);
        sendBn.addActionListener(this);
    }

    private void displayLoginView() {
        loginView = new JFrame("登录");
        loginView.setLayout(new GridLayout(3, 1));
        loginView.setSize(400, 230);

        JPanel ip = new JPanel();
        JLabel label = new JLabel("   IP:");
        ip.add(label);
        ipEt = new JTextField(20);
        ip.add(ipEt);
        loginView.add(ip);

        JPanel name = new JPanel();
        JLabel label1 = new JLabel("昵称:");
        name.add(label1);
        nameEt = new JTextField(20);
        name.add(nameEt);
        loginView.add(name);

        JPanel btnView = new JPanel();
        JButton login = new JButton("登陆");
        btnView.add(login);
        JButton cancle = new JButton("取消");
        btnView.add(cancle);
        loginView.add(btnView);

        loginView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setWindowCenter(loginView, 400, 260, true);

        login.addActionListener(this);
        cancle.addActionListener(this);
    }

    private static void setWindowCenter(JFrame frame, int width, int height, boolean flag) {
        Dimension ds = frame.getToolkit().getScreenSize();

        int width1 = ds.width;
        int height1 = ds.height;

        frame.setLocation(width1 / 2 - width / 2, height1 / 2 - height / 2);
        frame.setVisible(flag);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        switch (btn.getText()) {
            case "登陆":
                String ip = ipEt.getText().toString();
                String name = nameEt.getText().toString();
                String msg = "";
                if (ip == null || !ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                    msg = "请输入合法的服务端 IP 地址";
                } else if (name == null || !name.matches("\\S{1,}")) {
                    msg = "昵称必须 1 个字符以上";
                }

                if (!msg.equals("")) {
                    JOptionPane.showMessageDialog(loginView, msg);
                } else {
                    try {
                        win.setTitle(name);
                        socket = new Socket(ip, Constants.PORT);

                        new ClientReader(this, socket).start();

                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                        dos.writeInt(1);
                        dos.writeUTF(name.trim());
                        dos.flush();

                        loginView.dispose();
                        displayChatView();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                break;
            case "取消":
                System.exit(0);
                break;
            case "发送":
                String msgSend = smsSend.getText().toString();
                if (!msgSend.trim().equals("")) {
                    try {
                        String selectName = onLineUsers.getSelectedValue();
                        int flag = 2;
                        if (selectName != null && !selectName.equals("")) {
                            msgSend = ("@" + selectName + " " + msgSend);
                            if (isPrivateBn.isSelected()) {
                                flag = 3;
                            }
                        }
                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                        dos.writeInt(flag);
                        dos.writeUTF(msgSend);
                        if (flag == 3) {
                            dos.writeUTF(selectName.trim());
                        }
                        dos.flush();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                smsSend.setText(null);
                break;
        }
    }
}
