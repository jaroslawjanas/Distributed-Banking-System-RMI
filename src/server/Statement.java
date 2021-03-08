package server;

import utils.Color;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Statement implements Serializable {
    List<Transaction> transactions;

    public Statement(List<Transaction> statementTransactions) {
        transactions = statementTransactions;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();

        for (Transaction transaction:transactions) {
            DecimalFormat df = new DecimalFormat("#,###.00");
            String balance= df.format(transaction.getAmount());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:MM");
            LocalDateTime date = transaction.getDate();

            out.append(Color.PURPLE).append("Date: ").append(Color.YELLOW).append(date.format(formatter)).append("\t")
                    .append(Color.CYAN).append(transaction.getDesctiprion()).append(" \t\t")
                    .append(Color.YELLOW).append("€").append(balance).append(Color.RESET).append(System.lineSeparator());
        }

        return out.toString();
    }
}
