package com.example.websocket.rmi.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    public static void main(String args[]) throws Exception
    {
        DatagramSocket clientSocket = new DatagramSocket();

        InetAddress IPAddress = InetAddress.getByName("localhost");

        int serverPort = 2023;

        String message = "hello";

        byte[] sendData = message.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort);

        clientSocket.send(sendPacket);

//        byte[] sendData;
//        byte[] receiveData = new byte[1024];
//        sendData = "getDate".getBytes();
//        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 2023);
//        clientSocket.send(sendPacket);
//        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//        clientSocket.receive(receivePacket);
//        String str = new String(receivePacket.getData(), 0, receivePacket.getLength());
//        System.out.println(str);
//        clientSocket.close();
    }
}
