package server;

import java.io.Serializable;

public class Access implements Serializable {
    
    private final long sessionId;
    private final long accountNumber;
    
    Access(long sessionId, long accountNumber) {
        this.sessionId = sessionId;
        this.accountNumber = accountNumber;
    }

    public long getSessionId() {
        return sessionId;
    }

    public long getAccountNumber() {
        return accountNumber;
    }
}
