package server;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Account {
    public static long newestAccount = 100000;
    public long accountNumber;
    public String username;
    public String hashedPassword;
    public List<Transaction> transactions;
    public BigDecimal balance;

    public Account(String username, String hashedPassword){
        this.username = username;
        this.hashedPassword = hashedPassword;
        newestAccount++;
        accountNumber= newestAccount;
    }

    public void deposit(BigDecimal deposit){
        balance = balance.add(deposit);
        Transaction newTransaction = new Transaction(deposit, LocalDateTime.now(), "deposit");
        addTransaction(newTransaction);
    }

    public void withdraw(BigDecimal withdraw){
        balance = balance.subtract(withdraw);
        Transaction newTransaction = new Transaction(withdraw, LocalDateTime.now(), "withdraw");
        addTransaction(newTransaction);
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

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }

    public BigDecimal getBalance(){
        return balance;
    }
}
