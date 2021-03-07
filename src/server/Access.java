package server;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Access implements Serializable {
    
    private final long sessionId;
    private final long accountNumber;
    private final LocalDateTime validUntil;
    
    Access(long sessionId, long accountNumber, LocalDateTime validUntil) {
        this.sessionId = sessionId;
        this.accountNumber = accountNumber;
        this.validUntil = validUntil;
    }

    public long getSessionId() {
        return sessionId;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }
}
