package com.example.websocket.udp.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {
    public static void main(String[] args) throws IOException {
        DatagramSocket serverSocket = new DatagramSocket(9875);
        System.out.println("Server is started");
        byte[] receiveData = new byte[1024];
        byte[] sendData;

        // Thư mục gốc nơi lưu trữ các tài khoản
        String rootDirectory = "D:\\udp";
        File rootDir = new File(rootDirectory);
        if (!rootDir.exists()) {
            rootDir.mkdir(); // Tạo thư mục gốc nếu chưa có
        }
        while (true){
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            String receiceMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());


            String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
            // Tách yêu cầu từ client
            String[] parts = clientMessage.split(":", 2);
            String command = parts[0];
            String content = parts.length > 1 ? parts[1] : "";
            switch (command) {
                case "CREATE_ACCOUNT":
                    handleCreateAccount(content, serverSocket, IPAddress, port, rootDirectory);
                    break;
                case "SEND_EMAIL":
                    handleSendEmail(content, serverSocket, IPAddress, port, rootDirectory);
                    break;
                case "LOGIN":
                    handleLogin(content, serverSocket, IPAddress, port, rootDirectory);
                    break;
                default:
                    sendData = "Invalid command".getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    serverSocket.send(sendPacket);
                    break;
            }

        }
    }
    public static void handleCreateAccount(String accountName, DatagramSocket serverSocket, InetAddress IPAddress, int port, String rootDirectory) throws IOException {
        File accountDir = new File(rootDirectory, accountName);
        if (!accountDir.exists()) {
            accountDir.mkdir();
            File newEmailFile = new File(accountDir, "new_email.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(newEmailFile));
            writer.write("Thank you for using this service. we hope that you will feel comfortable....");
            writer.close();
            String response = "success";
            sendResponse(serverSocket, response, IPAddress, port);
        } else {
            String response = "Account already exists.";
            sendResponse(serverSocket, response, IPAddress, port);
        }
    }
    // Xử lý đăng nhập
    public static void handleLogin(String accountName, DatagramSocket serverSocket, InetAddress IPAddress, int port, String rootDirectory) throws IOException {
        File accountDir = new File(rootDirectory, accountName);
        if (accountDir.exists()) {
            File[] files = accountDir.listFiles();
            System.out.println(files.toString());
            StringBuilder fileList = new StringBuilder("Emails:");
            fileList.append(accountName).append(",");
            fileList.append("content:");
            for (File file : files) {
                fileList.append(file.getName()).append(",");
            }
//            fileList.append("status:success");
            sendResponse(serverSocket, fileList.toString(), IPAddress, port);
        } else {
            String response = "Account does not exist.";
            sendResponse(serverSocket, response, IPAddress, port);
        }
    }
    // Xử lý gửi email
    public static void handleSendEmail(String content, DatagramSocket serverSocket, InetAddress IPAddress, int port, String rootDirectory) throws IOException {
        String[] emailParts = content.split(":", 2);
        String accountName = emailParts[0];
        String emailContent = emailParts[1];

        File accountDir = new File(rootDirectory, accountName);
        if (accountDir.exists()) {
            String fileName = "email_" +"name"+ ".txt";
            File emailFile = new File(accountDir, fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(emailFile));
            writer.write(emailContent);
            writer.close();
            String response = "Email sent to " + accountName;
            sendResponse(serverSocket, response, IPAddress, port);
        } else {
            String response = "Account does not exist.";
            sendResponse(serverSocket, response, IPAddress, port);
        }
    }
    // Gửi phản hồi về client
    public static void sendResponse(DatagramSocket serverSocket, String response, InetAddress IPAddress, int port) throws IOException {
        byte[] sendData = response.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
        serverSocket.send(sendPacket);
    }
}
