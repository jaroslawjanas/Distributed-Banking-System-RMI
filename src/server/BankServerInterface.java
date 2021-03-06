package server;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BankServerInterface extends Remote {
    String ping() throws RemoteException;

    void createAccount(String username, long hashedPassword) throws RemoteException;

    Access login(String username, long hashedPassword) throws RemoteException;

    BigDecimal deposit(Access access, BigDecimal amount) throws RemoteException;

    BigDecimal balance(Access access) throws RemoteException;

    BigDecimal withdraw(Access access, BigDecimal amount) throws RemoteException;
}