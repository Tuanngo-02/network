package com.example.websocket.rmi.server;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;
import java.util.SimpleTimeZone;

public class UDPServer {
    public static void main(String args[]) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(2023);
        System.out.println("Server is started");
        byte[] receiveData = new byte[1024];
        while (true){
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            String receiceMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

            System.out.println(receiceMessage);

        }
        //        byte[] sendData;
//        while (true) {
//            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//            serverSocket.receive(receivePacket);
//            InetAddress IPAddress = receivePacket.getAddress();
//            int port = receivePacket.getPort();
//
//            String request = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
//            System.out.println("Received: " + request);
//
//            if (request.equals("getDate")) {
//                sendData = new Date().toString().getBytes();
//            } else {
//                sendData = "Server not know what you want?".getBytes();
//            }
//
//            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
//            serverSocket.send(sendPacket);
//        }
    }
}

