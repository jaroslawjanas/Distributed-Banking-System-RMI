package client;

import client.errors.*;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class AtmClient {

    private static BankServerInterface bankServer;
    private static Access access = null;

    public static void main (String[] args) {

        System.out.println(Color.YELLOW + "Connecting to the Bank..." + Color.RESET);
        try {
            bankServer = (BankServerInterface) Naming.lookup("//localhost/Bank");

            System.out.println(Color.YELLOW + "Pinging Bank Server..." + Color.RESET);
            ping();

            System.out.println(Color.GREEN + "Server connection established!" + Color.RESET);
            helloMsg();

        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            System.out.println(e.getMessage());
        }

        boolean exit = false;
        while(!exit) {
            String command = null;

            System.out.print(Color.YELLOW + "\n> " + Color.RESET);
            try {
                InputStreamReader inputStream = new InputStreamReader(System.in);
                BufferedReader reader = new BufferedReader(inputStream);

                command = reader.readLine();

            } catch (IOException e) {
                System.out.println(e.getMessage());
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

                        case "help":
                            if (commandArgs.length != 1) throw new ArgumentError();
                            help();
                            break;

                        case "clear":
                            if (commandArgs.length != 1) throw new ArgumentError();
                            clearConsole();
                            break;

                        case "hello":
                            if (commandArgs.length !=1) throw  new ArgumentError();
                            helloMsg();
                            break;

                        default:
                            throw new CommandError();
                    }
                } catch (RemoteException e) {
                    System.out.println(e.getCause().getMessage());
                } catch (ArgumentError | CommandError | NotLoggedInError | NumberError | IOException | DateFormatError e){
                    System.out.println(e.getMessage());
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
    }

    private static void createAccount(String username, String password) throws RemoteException {
        bankServer.createAccount(username, hashPassword(password));
        System.out.println(Color.GREEN + "Created a new account. Use your username \"" + Color.YELLOW + username + Color.GREEN + "\" to login." + Color.RESET);
    }

    private static void deposit(String amount) throws RemoteException, NotLoggedInError, NumberError {
        if(access == null) throw new NotLoggedInError();

        BigDecimal depositAmount = stringToBigDecimal(amount);
        BigDecimal newBalance = bankServer.deposit(access, depositAmount);
        System.out.println(Color.GREEN + "Balance: " + Color.YELLOW + "€" + newBalance + Color.RESET);
    }

    private static BigDecimal stringToBigDecimal(String number) throws NotLoggedInError, NumberError {
        if(access == null) throw new NotLoggedInError();
        if(number.contains("e")) throw new NumberError(); //do not allow exponents

        BigDecimal decimalNumber = null;
        try {
            decimalNumber = new BigDecimal(number);
        } catch (NumberFormatException e) {
            throw new NumberError();
        }
        return decimalNumber;
    }

    private static void balance() throws RemoteException, NotLoggedInError {
        if(access == null) throw new NotLoggedInError();
        BigDecimal balance = bankServer.balance(access);
        System.out.println(Color.GREEN + "Balance: " + Color.YELLOW  + "€" + balance + Color.RESET);
    }

    private static void withdraw(String amount) throws NotLoggedInError, RemoteException, ArgumentError, NumberError {
        if(access == null) throw new NotLoggedInError();

        BigDecimal withdrawAmount = stringToBigDecimal(amount);
        if(withdrawAmount==null) throw new ArgumentError();
        BigDecimal newBalance = bankServer.withdraw(access, withdrawAmount);
        System.out.println(Color.GREEN + "Balance: " + Color.YELLOW + "€" + newBalance + Color.RESET);
    }

    private static void ping() throws RemoteException {
        String pingRes = bankServer.ping();
        System.out.println(Color.GREEN + "Server says \"" + Color.YELLOW + pingRes + Color.GREEN + "\"" + Color.RESET);
    }

    private static void statement(String fromDateStr, String toDateStr) throws RemoteException, NotLoggedInError, DateFormatError {
        if(access == null) throw new NotLoggedInError();
        LocalDateTime fromDate = stringToLocalDateTime(fromDateStr, true);
        LocalDateTime toDate = stringToLocalDateTime(toDateStr, false);

        System.out.println( Color.GREEN + "Getting account statement for time period from: " +
                Color.YELLOW + localDateTimeToString(fromDate) + Color.GREEN + " to: " +
                Color.YELLOW + localDateTimeToString(toDate) + Color.RESET
        );

        Statement statement = bankServer.statement(access, fromDate, toDate);
        System.out.println(statement);
    }

    private static LocalDateTime stringToLocalDateTime(String str, boolean fromDate) throws DateFormatError {
        if(str.equalsIgnoreCase("now")) {
            return LocalDateTime.now();
        }

        if(str.equalsIgnoreCase("*")) {
            return LocalDateTime.MIN;
        }

        LocalDateTime datetime = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(str, formatter);

            // if today return now
            if(date.equals(LocalDate.now())) datetime = LocalDateTime.now();
            // otherwise return 0:00 or 23:59 time depending if "from" or "to"
            else {
                if(fromDate)
                    datetime = LocalDateTime.of(date, LocalTime.MIN);
                else
                    datetime = LocalDateTime.of(date, LocalTime.MAX);
            }

        } catch (DateTimeParseException e) {
            throw new DateFormatError();
        }

        return datetime;
    }

    private static String localDateTimeToString(LocalDateTime date) {
        if(date.isEqual(LocalDateTime.MIN)) {
            return "*";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    private static void help(){
        System.out.println(Color.GREEN +
        "Bank System commands:\n"+
        " > create [username] [password]  - Create a new user account\n"+
        " > login [username] [password]   - Login to existing user account\n"+
        " > balance                       - Returns current bank balance\n"+
        " > deposit [amount]              - Deposit specified amount into account\n"+
        " > withdraw [amount]             - Withdraw specified amount into account\n"+
        " > statement [from] [to]         - Creates a statement between specified dates. Use inputs \"* now\" to return all transactions or use the following date format: dd/MM/yyyy\n"+
        " > help                          - Displays this message\n"+
        " > ping                          - Test connection with the bank server\n"+
        " > logout                        - Logout current user\n"+
        " > exit                          - Exits the system\n"+
        " > clear                         - Clears the console\n"+
        " > hello                         - Displays the welcome message"+ Color.RESET);
    }

    private static void helloMsg() {
        System.out.println(
                Color.BGREEN + "                          __                    __\n" +
                Color.BGREEN + "          __       __     \\_\\  __          __   \\_\\  __   __       __\n" +
                Color.BGREEN + "          \\_\\     /_/        \\/_/         /_/      \\/_/   \\_\\     /_/\n" +
                Color.BGREEN + "        .-.  \\.-./  .-.   .-./  .-.   .-./  .-.   .-\\   .-.  \\.-./  .-.\n" +
                Color.BGREEN + "       //-\\\\_//-\\\\_//-\\\\_//-\\\\_//-\\\\_//-\\\\_// \\\\_//-\\\\_//-\\\\_//-\\\\_//-\\\\\n" +
                Color.BGREEN + "     __(   '-'   '-'\\  '-'   '-'  /'-'   '-'\\__'-'   '-'__/'-'   '-'\\__\n" +
                Color.BGREEN + "    /_/))            \\__       __/\\          \\_\\       /_/           \\_\\\n" +
                Color.BGREEN + " ___\\_//              \\_\\     /_/  \\__\n" +
                Color.BGREEN + "/_/  ((                             \\_\\\n" +
                Color.BGREEN + "      )) __         \n" +
                Color.BGREEN + "__   // /_/     " + Color.RESET + Color.YELLOW + "         ___       __   __         ___    ___  __   \n" +
                Color.BGREEN + "\\_\\_((_/___   " + Color.RESET + Color.YELLOW + "     |  | |__  |    /  ` /  \\  |\\/| |__      |  /  \\  \n" +
                Color.BGREEN + "     ))  \\_\\  " + Color.RESET + Color.YELLOW + "     |/\\| |___ |___ \\__, \\__/  |  | |___     |  \\__/ \n" +
                Color.BGREEN + "     \\\\\n" +
                Color.BGREEN + "      )) __     " + Color.RESET + Color.YELLOW + "                   ___       ___      \n" +
                Color.BGREEN + "__   // /_/     " + Color.RESET + Color.YELLOW + "                    |  |__| |__      \n" +
                Color.BGREEN + "\\_\\_((_/___   " + Color.RESET + Color.YELLOW + "                      |  |  | |___  \n" +
                Color.BGREEN + "     ))  \\_\\    \n" +
                Color.BGREEN + "     \\\\       " + Color.RESET + Color.YELLOW + "      __                     __   ___     __          \n" +
                Color.BGREEN + "      )) _      " + Color.RESET + Color.YELLOW + "   |__)  /\\  |\\ | |__/    /  \\ |__     |__)  |\\/| | \n" +
                Color.BGREEN + "__   // /_/     " + Color.RESET + Color.YELLOW + "   |__) /~~\\ | \\| |  \\    \\__/ |       |  \\  |  | | \n" +
                Color.BGREEN + "\\_\\_((_/___\n" +
                Color.BGREEN + "     ))  \\_\\                __                    __\n" +
                Color.BGREEN + "     \\\\     __       __     \\_\\  __          __   \\_\\  __   __       __\n" +
                Color.BGREEN + "  __  ))    \\_\\     /_/        \\/_/         /_/      \\/_/   \\_\\     /_/\n" +
                Color.BGREEN + "  \\_\\_((   .-.  \\.-./  .-.   .-./  .-.   .-./  .-.   .-\\   .-.  \\.-./  .-.\n" +
                Color.BGREEN + "       \\\\_//-\\\\_//-\\\\_//-\\\\_//-\\\\_//-\\\\_//-\\\\_// \\\\_//-\\\\_//-\\\\_//-\\\\_//-\\\\\n" +
                Color.BGREEN + "        '  \\__'-'   '-'\\  '-'   '-'  /'-'   '-'\\__'-'   '-'__/'-'   '-'\\__\n" +
                Color.BGREEN + "            \\_\\         \\__       __/\\          \\_\\       /_/           \\_\\\n" +
                Color.BGREEN + "                         \\_\\     /_/  \\__\n" +
                Color.BGREEN + "                                       \\_\\" + Color.RESET);
    }

    private static void clearConsole() throws IOException {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
