package server;

import server.errors.RemoteCreateAccountExists;
import server.errors.RemoteIncorrectLoginError;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BankServerInterface extends Remote {
    String ping() throws RemoteException;

    Access login(String username, long hashedPassword) throws RemoteException;
    void createAccount(String username, long hashedPassword) throws RemoteException;

}