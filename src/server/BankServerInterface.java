package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BankServerInterface extends Remote {
    String ping() throws RemoteException;

    String login(String username, String hashedPassword) throws RemoteException;

}