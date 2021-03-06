package server;

import server.errors.InputError;
import server.errors.OverdraftError;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Account {
    public static long newestAccount = 100000;
    public long accountNumber;
    public String username;
    public long hashedPassword;
    public List<Transaction> transactions;
    public BigDecimal balance;

    public Account(String username, long hashedPassword){
        this.username = username;
        this.hashedPassword = hashedPassword;
        newestAccount++;
        accountNumber= newestAccount;
    }

    public void deposit(BigDecimal deposit) throws InputError {
        if (deposit.compareTo(BigDecimal.ZERO) > 0) { //ensure input is a positive number
            balance = balance.add(deposit);
            Transaction newTransaction = new Transaction(deposit, LocalDateTime.now(), "deposit");
            addTransaction(newTransaction);
        } else {
            throw new InputError();
        }
    }

    public void withdraw(BigDecimal withdraw) throws OverdraftError, InputError {
        if (withdraw.compareTo(BigDecimal.ZERO) > 0) { //ensure input is a positive number
            if(balance.subtract(withdraw).compareTo(BigDecimal.ZERO)>=0) { //ensure there is enough money in the account to perform withdrawal
                balance = balance.subtract(withdraw);
                Transaction newTransaction = new Transaction(withdraw.multiply(new BigDecimal(-1)), LocalDateTime.now(), "withdraw");
                addTransaction(newTransaction);
            } else{
                throw new OverdraftError();
            }
        } else{
            throw new InputError();
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

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }

    public BigDecimal getBalance(){
        return balance;
    }
}
