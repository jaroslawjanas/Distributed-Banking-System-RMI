package client;

import server.BankServer;
import server.BankServerInterface;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class AtmClient {

    private static BankServerInterface bankServer;

    public static void main (String[] args) {

        System.out.println("Connecting to the Bank...");
        try {
            bankServer = (BankServerInterface) Naming.lookup("//localhost/Bank");

            System.out.println("Pinging Bank...");
            String pingRes = bankServer.ping();

            System.out.println("Bank says \"" + pingRes + "\"");
            System.out.println("Connection established");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
