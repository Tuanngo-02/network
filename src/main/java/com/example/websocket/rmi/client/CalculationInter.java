package com.example.websocket.rmi.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CalculationInter extends Remote {
    public double add (int a, int b) throws RemoteException;
    public double sub (int a, int b) throws RemoteException;
    public double convert (int f) throws RemoteException;
}
