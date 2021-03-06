package client;

import server.Access;
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
    private static Access access;

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

        boolean exit = false;
        while(!exit) {
            String command = null;

            System.out.print("> ");
            try {
                InputStreamReader inputStream = new InputStreamReader(System.in);
                BufferedReader reader = new BufferedReader(inputStream);

                command = reader.readLine();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if(command != null) {
                String[] commandArgs = command.split(" ");

                switch (commandArgs[0]) {
                    case "exit":
                        exit = true;
                        break;

                    case "login":
                        if (commandArgs.length != 3) {
                            System.out.println("[ Incorrect arguments! ]");
                            break;
                        }
                        login(commandArgs[1], commandArgs[2]);
                        break;

                    default:
                        System.out.println("[ Unrecognised command! ]");
                }

            }
        }
    }

    private static void login(String username, String password) {
        try {
            password = password.concat("salt_salt_salt");
            long hashedPassword = password.hashCode();

            access = bankServer.login(username, hashedPassword);
            System.out.println("Account Number: " + access.getAccountNumber());
            System.out.println("Session Id: " + access.getSessionId());

        } catch (RemoteException e) {
            System.out.println("[ Could not login! ]");
            e.printStackTrace();
        }
    }
}
