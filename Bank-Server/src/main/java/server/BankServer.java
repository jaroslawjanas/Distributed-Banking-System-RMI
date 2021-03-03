package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class BankServer implements BankServerInterface {

    BankServer() throws RemoteException {
        super();

    }

    public static void main(String[] args) {
        try {
            // First reset our Security manager
            //if (System.getSecurityManager() == null) {
            //    System.setSecurityManager(new SecurityManager());
            //    System.out.println("Security manager set");
            //}

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

    @Override
    public String login(String username, String hashedPassword) throws RemoteException {
        System.out.println("User " + username + " has logged in!");
        return "Session id: Hello World!";
    }
}