package server;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    // Needs some accessor methods to return information about the transaction
    private BigDecimal amount;
    private LocalDateTime date;
    private String description;

    public Transaction(BigDecimal amount, LocalDateTime date, String description){
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public LocalDateTime getDate(){
        return date;
    }

    public BigDecimal getAmount(){
        return amount;
    }

    public String getDesctiption(){
        return description;
    }

}