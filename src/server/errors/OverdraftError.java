package server.errors;

import utils.Color;

public class OverdraftError extends Throwable {
    public OverdraftError(){
        super(Color.RED + "[ Unable to complete withdrawal: Account balance too low. \nOverdrafts are not available on this account. Thank you. ]" + Color.RESET);
    }
}
