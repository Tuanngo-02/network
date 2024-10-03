package com.example.websocket.rmi.server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

//muốn send qua môi trường mạng (internet) đều phải implements Serializable, dọc ghi file phân bt đối tuong
//nhìu class muốn lấy ra được UnicastRemoteObject
public class CaculationImpl extends UnicastRemoteObject implements CalculationInter, Serializable {

    public CaculationImpl() throws RemoteException{

    }
    @Override
    public String add(double a, double b) throws RemoteException {
        return (a+b) + "lo cec";
    }

    @Override
    public double sub(double a, double b) throws RemoteException {
        return a-b;
    }

    @Override
    public double convert(double f) throws RemoteException {
        return f;
    }
}
