package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class BankServer implements BankServerInterface {

    public List<Account> accounts = new ArrayList<>();

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
            System.out.println("Server ready for requests!");
        }
        catch(Exception exc)
        {
            System.out.println("Error in main - " + exc.toString());
        }
    }

    public void createAccount(String username, String hashedPassword){
        Account newAccount = new Account(username, hashedPassword);
        accounts.add(newAccount);
        System.out.println("New Account created");
    }

    @Override
    public String login(String username, String hashedPassword) throws RemoteException {
        System.out.println("User " + username + " has logged in!");
        return "Session id: Hello World!";
    }
}