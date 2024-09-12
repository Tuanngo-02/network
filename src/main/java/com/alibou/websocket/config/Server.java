package com.alibou.websocket.config;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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

public class Server extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private DefaultListModel<String> model;
    private JPanel contentPane;
    private JTextField text_send;
    private JTextField port;
    private JList<String> lsHistory;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Server frame = new Server();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Server() {
        clients = new ArrayList<>();
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

        JLabel lblNewLabel = new JLabel("SERVER");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 32));
        lblNewLabel.setBounds(29, 0, 159, 39);
        panel.add(lblNewLabel);

        text_send = new JTextField();
        text_send.setBounds(115, 357, 370, 37);
        panel.add(text_send);
        text_send.setColumns(10);

        JButton send_BT = new JButton("Send");
        send_BT.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessageToClients(text_send.getText());
            }
        });
        send_BT.setFont(new Font("Tahoma", Font.PLAIN, 14));
        send_BT.setBounds(495, 357, 112, 39);
        panel.add(send_BT);

        port = new JTextField();
        port.setBounds(344, 5, 120, 25);
        panel.add(port);
        port.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("PORT");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_2.setBounds(288, 11, 47, 13);
        panel.add(lblNewLabel_2);

        JButton start_BT = new JButton("Start");
        start_BT.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });
        start_BT.setFont(new Font("Tahoma", Font.PLAIN, 14));
        start_BT.setBounds(474, 0, 96, 30);
        panel.add(start_BT);

        lsHistory = new JList<>();
        lsHistory.setBounds(10, 49, 597, 299);
        panel.add(lsHistory);

        JButton btnNewButton = new JButton("Disconnect");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                disconnectServer();
            }
        });
        btnNewButton.setBounds(230, 404, 167, 34);
        panel.add(btnNewButton);
    }

    private void startServer() {
        try {
            model.addElement("Server starting...");
            lsHistory.setModel(model);
            serverSocket = new ServerSocket(Integer.parseInt(port.getText()));
            model.addElement("Server started on port " + port.getText());
            lsHistory.setModel(model);

            // Start a thread to accept new clients
            Thread acceptClientsThread = new Thread(() -> {
                try {
                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        ClientHandler clientHandler = new ClientHandler(clientSocket);
                        clients.add(clientHandler);
                        model.addElement("Client connected: " + clientSocket.getInetAddress());
                        lsHistory.setModel(model);
                        new Thread(clientHandler).start();
                    }
                } catch (IOException e) {
                    model.addElement("Error accepting clients: " + e.getMessage());
                    lsHistory.setModel(model);
                }
            });
            acceptClientsThread.start();
        } catch (IOException e) {
            model.addElement("Error starting server: " + e.getMessage());
            lsHistory.setModel(model);
        }
    }

    private void sendMessageToClients(String message) {
        for (ClientHandler clientHandler : clients) {
            clientHandler.sendMessage(message);
        }
        model.addElement("Sent to clients: " + message);
        lsHistory.setModel(model);
    }

    private void disconnectServer() {
        try {
            for (ClientHandler clientHandler : clients) {
                clientHandler.disconnect();
            }
            serverSocket.close();
            model.addElement("Server disconnected");
            lsHistory.setModel(model);
        } catch (IOException e) {
            model.addElement("Error disconnecting server: " + e.getMessage());
            lsHistory.setModel(model);
        }
    }

    @Override
    public void run() {
        // Empty implementation, not used in this example
    }

    private class ClientHandler implements Runnable {

        private Socket socket;
        private DataInputStream input;
        private DataOutputStream output;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                model.addElement("Error creating I/O streams: " + e.getMessage());
                lsHistory.setModel(model);
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String message = input.readUTF();
                    model.addElement("Client (" + socket.getInetAddress() + "): " + message);
                    lsHistory.setModel(model);
                }
            } catch (IOException e) {
                model.addElement("Client disconnected: " + e.getMessage());
                lsHistory.setModel(model);
                disconnect();
            }
        }

        public void sendMessage(String message) {
            try {
                if (output != null) {
                    output.writeUTF(message);
                    output.flush();
                }
            } catch (IOException e) {
                model.addElement("Error sending message to client: " + e.getMessage());
                lsHistory.setModel(model);
            }
        }

        public void disconnect() {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                model.addElement("Error disconnecting client: " + e.getMessage());
                lsHistory.setModel(model);
            }
        }
    }
}
