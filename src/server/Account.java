package server;

import server.errors.InputRemoteError;
import server.errors.OverdraftRemoteError;

import java.io.*;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Account implements Serializable {
    private final int accountNumber;
    private final String username;
    private long hashedPassword;
    private List<Transaction> transactions;
    private BigDecimal balance;

    public Account(String username, long hashedPassword){
        int newestAccount = readInt();

        this.username = username;
        this.hashedPassword = hashedPassword;
        newestAccount++;
        accountNumber = newestAccount;
        balance = new BigDecimal(0);
        transactions = new ArrayList<>();

        saveInt(newestAccount);
    }

    public void deposit(BigDecimal amount) throws RemoteException {
        if (amount.compareTo(BigDecimal.ZERO) > 0) { //ensure input is a positive number
            balance = balance.add(amount);
            Transaction newTransaction = new Transaction(amount, LocalDateTime.now(), "deposit");
            addTransaction(newTransaction);
        } else {
            throw new InputRemoteError();
        }
    }

    public void withdraw(BigDecimal amount) throws RemoteException {
        if (amount.compareTo(BigDecimal.ZERO) > 0) { //ensure input is a positive number
            if(balance.subtract(amount).compareTo(BigDecimal.ZERO)>=0) { //ensure there is enough money in the account to perform withdrawal
                balance = balance.subtract(amount);
                Transaction newTransaction = new Transaction(amount, LocalDateTime.now(), "withdraw");
                addTransaction(newTransaction);
            } else{
                throw new OverdraftRemoteError();
            }
        } else{
            throw new InputRemoteError();
        }
    }

    public Statement constructStatement (LocalDateTime from, LocalDateTime to){
        List<Transaction> statementTransactions = new ArrayList<>();

        for (Transaction transaction:transactions) {
            if(transaction.getDate().isBefore(to) && transaction.getDate().isAfter(from)){
                statementTransactions.add(transaction);
            }
        }
        return new Statement(statementTransactions);
    }

    private void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }

    public BigDecimal getBalance(){
        return balance;
    }

    public long getHashedPassword() {
        return hashedPassword;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public String getUsername() {
        return username;
    }

    private static void saveInt(int number) {
        try {
            FileWriter fw = new FileWriter("../save/newestAccount.txt");
            fw.write(Integer.toString(number));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int readInt() {
        int number = (int) (Math.random() * 10000);
        try {
            File file = new File("../save/newestAccount.txt");
            if(!file.exists()) return number;
            Scanner scanner = new Scanner(file);
            number = Integer.parseInt(scanner.nextLine());
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return number;
    }
}
