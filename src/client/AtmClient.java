package client;

import client.errors.ArgumentError;
import client.errors.CommandError;
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

                try {
                    switch (commandArgs[0]) {
                        case "exit":
                            exit = true;
                            break;

                        case "login":
                            if (commandArgs.length != 3) throw new ArgumentError();
                            login(commandArgs[1], commandArgs[2]);
                            break;

                        case "create":
                            if (commandArgs.length != 3) throw new ArgumentError();
                            createAccount(commandArgs[1], commandArgs[2]);
                            break;

                        default:
                            throw new CommandError();
                    }
                } catch (ArgumentError | CommandError e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static long hashPassword(String password) {
        password = password.concat("salt_salt_salt");
        return password.hashCode();
    }

    private static void login(String username, String password) {
        try {
            access = bankServer.login(username, hashPassword(password));
        } catch (RemoteException e) {
            System.out.println("[ Could not login! ]");
            e.printStackTrace();
        }

        System.out.println("Account Number: " + access.getAccountNumber());
//        System.out.println("Session Id: " + access.getSessionId()); // hidden
    }

    private static void createAccount(String username, String password) {
        try {
            bankServer.createAccount(username, hashPassword(password));
        } catch (RemoteException e) {
            System.out.println("[ Could not create a new account! ]");
            e.printStackTrace();
        }
        System.out.println("Created a new account. Use your username \"" + username + "\" to login.");
    }
}
