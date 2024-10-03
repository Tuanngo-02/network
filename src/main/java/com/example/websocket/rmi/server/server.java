package com.example.websocket.rmi.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class server {
    public static void main(String[] args) {
        try {
            //tạo thanh ghi ở phái server
            Registry registry = LocateRegistry.createRegistry(1099);

            //tạo ra caculator
            CaculationImpl caculation = new CaculationImpl();

            //đăng ký đối tượng cho thanh ghi
            registry.rebind("cal", caculation);

            while (true){
                System.out.println("server run");
                Thread.sleep(5000);
            }
        }catch (Exception e) {
        }
    }
}
