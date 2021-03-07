package server;

import server.errors.*;
import utils.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BankServer implements BankServerInterface {

    public List<Account> accounts = new ArrayList<>();
    public List<Access> accesses = new ArrayList<>();

    BankServer() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        try {
            // First reset our Security manager
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
                System.out.println("Security manager set");
            }

            // Create an instance of the local object
            BankServerInterface bankServer = new BankServer();
            System.out.println("Instance of Bank Server created");
            BankServerInterface stub = (BankServerInterface) UnicastRemoteObject.exportObject(bankServer, 0);

            // Put the server object into the Registry
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("Bank", stub);
            System.out.println("Name rebind completed");
            System.out.println(Color.GREEN + "Server ready for requests" + Color.RESET);
        }
        catch(Exception exc)
        {
            System.out.println("Error in main - " + exc.toString());
        }
    }

    public String ping(){
        return "pong";
    }

    public void createAccount(String username, long hashedPassword) throws RemoteException {
        for (Account account : accounts) {
            if(account.getUsername().equalsIgnoreCase(username)) throw new CreateAccountExistsRemoteError();
        }
        Account newAccount = new Account(username, hashedPassword);
        accounts.add(newAccount);
        System.out.println(Color.YELLOW + "New Account \"" + username + "\" created" + Color.RESET);
    }

    public Access login(String username, long hashedPassword) throws RemoteException {
        for (Account account : accounts) {
            if(account.getUsername().equalsIgnoreCase(username) && account.getHashedPassword() == hashedPassword) {
                System.out.println(Color.YELLOW + "User \"" + username + "\" has logged in" + Color.RESET);

                LocalDateTime validUntil = LocalDateTime.now().plusMinutes(10);
                long sessionId = randomSessionId();

                Access access = new Access(sessionId, account.getAccountNumber(), validUntil);
                accesses.add(access);
                return access;
            }
        }

        throw new IncorrectLoginRemoteError();
    }

    private long randomSessionId(){
        long leftLimit = 100000000000L;
        long rightLimit = 100000000000000000L;
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }

    private Account verifyAccess(Access clientAccess) throws RemoteException {
        for(Access serverAccess : accesses) {
            if(serverAccess.getSessionId() == clientAccess.getSessionId()
            && serverAccess.getAccountNumber() == clientAccess.getAccountNumber()
            && serverAccess.getValidUntil().equals(clientAccess.getValidUntil())) {
//                check if still valid
                if(LocalDateTime.now().isBefore(serverAccess.getValidUntil())) {
                    long accountNumber = serverAccess.getAccountNumber();
                    for(Account account : accounts) {
                        if(account.getAccountNumber() == accountNumber) {
                            return account;
                        }
                    }
                }
                else {
                    throw new AccessExpiredRemoteError();
//                    Expired
                }
            }
        }
//        Could not find access obj
        throw new AccessDeniedRemoteError();
    }

    public BigDecimal deposit(Access access, BigDecimal amount) throws RemoteException {
        Account account = verifyAccess(access);
        account.deposit(amount);
        return account.getBalance();
    }

    public BigDecimal balance(Access access) throws RemoteException {
        Account account = verifyAccess(access);
        return account.getBalance();
    }

    public BigDecimal withdraw(Access access, BigDecimal amount) throws RemoteException {
        Account account = verifyAccess(access);
        account.withdraw(amount);
        return account.getBalance();
    }
}