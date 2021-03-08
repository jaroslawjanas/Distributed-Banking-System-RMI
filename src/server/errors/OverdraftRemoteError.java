package server.errors;

import utils.Color;
import java.rmi.RemoteException;

public class OverdraftRemoteError extends RemoteException {

    public OverdraftRemoteError(){
        super(Color.RED + "[ Account balance too low. \nOverdrafts are not available on this account. ]" + Color.RESET);
    }
}
