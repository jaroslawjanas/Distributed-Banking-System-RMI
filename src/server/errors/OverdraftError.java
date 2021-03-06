package server.errors;

import utils.Color;

import java.rmi.RemoteException;

public class OverdraftError extends RemoteException {

    public OverdraftError(){
        super(Color.RED + "[ Unable to complete withdrawal: Account balance too low. \nOverdrafts are not available on this account. Thank you. ]" + Color.RESET);
    }
}
