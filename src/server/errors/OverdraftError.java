package server.errors;

public class OverdraftError extends Throwable {
    public OverdraftError(){
        super("Unable to complete withdrawal: Account balance too low. \nOverdrafts are not available on this account. Thank you.");
    }
}
