package server;

import utils.Color;
import java.io.Serializable;
import java.math.BigDecimal;
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
        BigDecimal totalBalance  = new BigDecimal(0);
        BigDecimal totalWithdraw = new BigDecimal(0);
        BigDecimal totalDeposit  = new BigDecimal(0);
        StringBuilder out = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#,##0.00");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String totalBalanceAmount;

        //header
        out.append(Color.PURPLE).append("Transaction Date")
                .append(Color.CYAN).append("   Details").append(Color.RESET)
                .append(Color.BBLUE).append("\tWithdrawals").append("\tDeposits").append(Color.RESET)
                .append(Color.BGREEN).append("\tBalance").append(Color.RESET)
                .append(Color.RESET).append(System.lineSeparator());

        for (Transaction transaction:transactions) {
            LocalDateTime date = transaction.getDate();

            //date and description
            out.append(Color.YELLOW).append(date.format(formatter)).append("   ")
                    .append(Color.BWHITE).append(transaction.getDesctiption()).append(Color.RESET);

            String transactionAmount= df.format(transaction.getAmount());

            if(transaction.getDesctiption().equals("deposit")){
                totalDeposit = totalDeposit.add(transaction.getAmount());
                totalBalance = totalBalance.add(transaction.getAmount());

                // transaction amount with withdrawal/deposit spacing
                out.append("\t\t\t").append(Color.YELLOW).append("€").append(transactionAmount).append("   ");
            } else {
                totalWithdraw = totalWithdraw.add(transaction.getAmount());
                totalBalance = totalBalance.subtract(transaction.getAmount());

                // transaction amount with withdrawal/deposit spacing
                out.append("\t").append(Color.YELLOW).append("€").append(transactionAmount);
                if(transactionAmount.length() < 8) out.append("\t\t\t");
                else out.append("\t\t");
            }

            totalBalanceAmount = df.format(totalBalance);
            //balance after this transaction
            out.append("\t").append("€").append(totalBalanceAmount).append(Color.RESET).append(System.lineSeparator());
        }

        //final balances
        out.append("----------------------------------------------------------------------------\n")
                .append(Color.CYAN).append("Closing Balances:").append("\t\t")
                .append(Color.YELLOW).append("€").append(df.format(totalWithdraw)).append("\t");

        if(totalWithdraw.toString().length() < 6) out.append("\t");

        out.append(Color.YELLOW).append("€").append(df.format(totalDeposit)).append("\t")
                .append(Color.YELLOW).append("€").append(df.format(totalBalance));

        return out.toString();
    }
}
