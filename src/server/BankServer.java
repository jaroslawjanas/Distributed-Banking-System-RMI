package server;

import server.errors.RemoteCreateAccountExists;
import server.errors.RemoteIncorrectLoginError;
import utils.Color;
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

    public void createAccount(String username, long hashedPassword) throws RemoteException{
        for (Account account : accounts) {
            if(account.getUsername().equalsIgnoreCase(username)) throw new RemoteCreateAccountExists();
        }
        Account newAccount = new Account(username, hashedPassword);
        accounts.add(newAccount);
        System.out.println(Color.YELLOW + "New Account \"" + username + "\" created" + Color.RESET);
    }

    public Access login(String username, long hashedPassword) throws RemoteException{
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

        throw new RemoteIncorrectLoginError();
    }

    private long randomSessionId(){
        long leftLimit = 100000000000L;
        long rightLimit = 100000000000000000L;
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }
}