package com.example.websocket.rmi.client;

import com.example.websocket.rmi.server.CaculationImpl;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class client {
    public static void main(String[] args) {
        try {
            System.out.println("....");
           //lấy được thanh ghi từ máy chủ từ xa
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            System.out.println("....");
            //Tìm kiếm cái object để nó thực thi
//            CalculationInter cal = (CalculationInter) registry.lookup("cal");
//            System.out.println("....");
            Remote remoteService = Naming.lookup ("cal");
            System.out.println("....");
            // Cast to a RMICalcul Interface
            CaculationImpl calculService = (CaculationImpl) remoteService;
            System.out.println(calculService.convert(4));
            System.out.println(calculService.add(3,4));
//            //thực thi
//            Scanner sc = new Scanner(System.in);
//            System.out.println("nhap a");
//            double a = sc.nextDouble();
//            System.out.println("nhap b");
//            double b = sc.nextDouble();
//            double result = cal.add(a,b);
//            System.out.println(result);
        }catch (Exception e) {
        }
    }
}
