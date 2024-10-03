package com.example.websocket.TCP;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

public class Client extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField port;
    private JList<String> lsHistory;
    private DefaultListModel<String> model;
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;

    public static void main(String[] args) throws UnknownHostException {
        EventQueue.invokeLater(() -> {
            try {
                Client frame = new Client();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Client() {
        model = new DefaultListModel<>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 641, 495);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        panel.setBounds(10, 10, 617, 448);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel id_client = new JLabel("CLIENT");
        id_client.setFont(new Font("Tahoma", Font.PLAIN, 32));
        id_client.setBounds(25, 4, 159, 39);
        panel.add(id_client);

        textField = new JTextField();
        textField.setBounds(10, 399, 475, 39);
        panel.add(textField);
        textField.setColumns(10);

        JButton send_BT = new JButton("Send");
        send_BT.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        send_BT.setFont(new Font("Tahoma", Font.PLAIN, 14));
        send_BT.setBounds(495, 397, 112, 39);
        panel.add(send_BT);

        port = new JTextField();
        port.setBounds(248, 14, 141, 29);
        panel.add(port);
        port.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Port");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel_1.setBounds(214, 12, 35, 29);
        panel.add(lblNewLabel_1);

        JButton connect_BT = new JButton("Connect");
        connect_BT.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });
        connect_BT.setBounds(400, 14, 85, 29);
        panel.add(connect_BT);

        JButton disconnect_BT = new JButton("Disconnect");
        disconnect_BT.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                disconnectFromServer();
            }
        });
        disconnect_BT.setBounds(495, 14, 112, 29);
        panel.add(disconnect_BT);

        lsHistory = new JList<>();
        lsHistory.setBounds(10, 63, 597, 326);
        panel.add(lsHistory);
    }

    private void connectToServer() {
        try {
            model.addElement("Client connecting...");
            lsHistory.setModel(model);
            socket = new Socket("25.3.237.134",Integer.parseInt(port.getText()));
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());


            // Start a thread to listen for messages from the server
            Thread serverListenerThread = new Thread(() -> {
                try {
                    while (true) {
                        String message = input.readUTF();
                        model.addElement("Server: " + message);
                        lsHistory.setModel(model);
                    }
                } catch (IOException e) {
                    model.addElement("Connection lost: " + e.getMessage());
                    lsHistory.setModel(model);
                }
            });
            serverListenerThread.start();

            model.addElement("Client is connected");
            lsHistory.setModel(model);
        } catch (IOException e) {
            model.addElement("Error connecting to server: " + e.getMessage());
            lsHistory.setModel(model);
        }
    }

    private void sendMessage() {
        try {
            if (output != null) {
                String message = textField.getText();
                output.writeUTF(message);
                output.flush();
                model.addElement("Sent: " + message);
                lsHistory.setModel(model);
            }
        } catch (IOException e) {
            model.addElement("Error sending message: " + e.getMessage());
            lsHistory.setModel(model);
        }
    }

    private void disconnectFromServer() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                model.addElement("Disconnected from server");
                lsHistory.setModel(model);
            }
        } catch (IOException e) {
            model.addElement("Error disconnecting: " + e.getMessage());
            lsHistory.setModel(model);
        }
    }
}
