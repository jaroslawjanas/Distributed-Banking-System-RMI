package client;

import client.errors.ArgumentError;
import client.errors.CommandError;
import server.errors.DateRangeRemoteError;
import client.errors.NotLoggedInError;
import server.Access;
import server.BankServerInterface;
import server.Statement;
import utils.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class AtmClient {

    private static BankServerInterface bankServer;
    private static Access access = null;

    public static void main (String[] args) {

        System.out.println("Connecting to the Bank...");
        try {
            bankServer = (BankServerInterface) Naming.lookup("//localhost/Bank");

            System.out.println("Pinging Bank...");
            ping();

            System.out.println(Color.GREEN + "Connection established!" + Color.RESET);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }

        boolean exit = false;
        while(!exit) {
            String command = null;

            System.out.print(Color.YELLOW + "> " + Color.RESET);
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
                            if (commandArgs.length !=1) throw new ArgumentError();
                            exit = true;
                            System.out.println(Color.YELLOW + "Exiting..." + Color.RESET);
                            break;

                        case "logout":
                            if (commandArgs.length != 1) throw new ArgumentError();
                            access = null;
                            System.out.println(Color.YELLOW + "You have been logged out!" + Color.RESET);
                            break;

                        case "login":
                            if (commandArgs.length != 3) throw new ArgumentError();
                            login(commandArgs[1], commandArgs[2]);
                            break;

                        case "create":
                            if (commandArgs.length != 3) throw new ArgumentError();
                            createAccount(commandArgs[1], commandArgs[2]);
                            break;

                        case "deposit":
                            if (commandArgs.length != 2) throw new ArgumentError();
                            deposit(commandArgs[1]);
                            break;

                        case "balance":
                            if (commandArgs.length != 1) throw new ArgumentError();
                            balance();
                            break;

                        case "withdraw":
                            if (commandArgs.length != 2) throw new ArgumentError();
                            withdraw(commandArgs[1]);
                            break;

                        case "ping":
                            if (commandArgs.length !=1) throw new ArgumentError();
                            ping();
                            break;

                        case "statement":
                            if (commandArgs.length != 3) throw new ArgumentError();
                            statement(commandArgs[1], commandArgs[2]);
                            break;

                        default:
                            throw new CommandError();
                    }
                } catch (ArgumentError | CommandError | RemoteException | NotLoggedInError e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static long hashPassword(String password) {
        password = password.concat("salt_salt_salt");
        return password.hashCode();
    }

    private static void login(String username, String password) throws RemoteException {
        access = bankServer.login(username, hashPassword(password));


        System.out.println(Color.GREEN + "Account Number: " + Color.YELLOW + access.getAccountNumber() + Color.RESET);
//        System.out.println("Session Id: " + access.getSessionId()); // hidden
    }

    private static void createAccount(String username, String password) throws RemoteException {
        bankServer.createAccount(username, hashPassword(password));
        System.out.println(Color.GREEN + "Created a new account. Use your username \"" + Color.YELLOW + username + Color.GREEN + "\" to login." + Color.RESET);
    }

    private static void deposit(String amount) throws RemoteException, NotLoggedInError {
        if(access == null) throw new NotLoggedInError();

        BigDecimal depositAmount = stringToBigDecimal(amount);
        BigDecimal newBalance = bankServer.deposit(access, depositAmount);
        System.out.println(Color.GREEN + "Balance: " + Color.YELLOW + newBalance + Color.RESET);
    }

    private static BigDecimal stringToBigDecimal(String number) throws NotLoggedInError {
        if(access == null) throw new NotLoggedInError();

        BigDecimal decimalNumber = null;
        try {
            decimalNumber = new BigDecimal(number);
        } catch (NumberFormatException e) {
            System.out.println(Color.RED + "[ The amount must be a number! ]" + Color.RESET);
            e.printStackTrace();
        }
        return decimalNumber;
    }

    private static void balance() throws RemoteException, NotLoggedInError {
        if(access == null) throw new NotLoggedInError();
        BigDecimal balance = bankServer.balance(access);
        System.out.println(Color.GREEN + "Balance: " + Color.YELLOW + balance + Color.RESET);
    }

    private static void withdraw(String amount) throws NotLoggedInError, RemoteException {
        if(access == null) throw new NotLoggedInError();

        BigDecimal withdrawAmount = stringToBigDecimal(amount);
        BigDecimal newBalance = bankServer.withdraw(access, withdrawAmount);
        System.out.println(Color.GREEN + "Balance: " + Color.YELLOW + newBalance + Color.RESET);
    }

    private static void ping() throws RemoteException {
        String pingRes = bankServer.ping();
        System.out.println(Color.GREEN + "Bank says \"" + Color.YELLOW + pingRes + Color.GREEN + "\"" + Color.RESET);
    }

    private static void statement(String fromDateStr, String toDateStr) throws RemoteException, NotLoggedInError {
        if(access == null) throw new NotLoggedInError();
        LocalDateTime fromDate = stringToLocalDateTime(fromDateStr);
        LocalDateTime toDate = stringToLocalDateTime(toDateStr);

        System.out.println( Color.GREEN + "Getting account statement for time period from: " +
                Color.YELLOW + localDateTimeToString(fromDate) + Color.GREEN + " to: " +
                Color.YELLOW + localDateTimeToString(toDate) + Color.RESET
        );

        Statement statement = bankServer.statement(access, fromDate, toDate);
        System.out.println(statement);
    }

    private static LocalDateTime stringToLocalDateTime(String str) {
        if(str.equalsIgnoreCase("now")) {
            return LocalDateTime.now();
        }

        if(str.equalsIgnoreCase("*")) {
            return LocalDateTime.MIN;
        }

        LocalDateTime date = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            date = LocalDateTime.parse(str, formatter);
        } catch (DateTimeParseException e) {
            System.out.println(Color.RED + "[ Incorrect date format! The date must be in \"dd/MM/yyyy\" format. ]" + Color.RESET);
            e.printStackTrace();
        }

        return date;
    }

    private static String localDateTimeToString(LocalDateTime date) {
        if(date.isEqual(LocalDateTime.MIN)) {
            return "*";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }
}
