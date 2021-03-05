package server;

import java.text.DecimalFormat;
import java.util.List;

public class Statement {
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
            out.append(String.format("Date: %s\t%s\t €%s", transaction.getDate().toString(), transaction.getDesctiprion(), balance));
        }

        return out.toString();
    }
}
