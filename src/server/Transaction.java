package server;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    // Needs some accessor methods to return information about the transaction
    private BigDecimal money;
    private LocalDateTime date;
    private String description;

    public Transaction(BigDecimal money, LocalDateTime date, String description){
        this.money = money;
        this.date = date;
        this.description = description;
    }

    public LocalDateTime getDate(){
        return date;
    }

    public BigDecimal getAmount(){
        return money;
    }

    public String getDesctiprion(){
        return description;
    }

}