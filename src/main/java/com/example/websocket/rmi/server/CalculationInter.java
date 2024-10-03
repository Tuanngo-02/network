package com.example.websocket.rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CalculationInter extends Remote {
    public String add (double a, double b) throws RemoteException;
    public double sub (double a, double b) throws RemoteException;
    public double convert (double f) throws RemoteException;
}
