package server.errors;

import utils.Color;
import java.rmi.RemoteException;

public class RemoteCreateAccountExists extends RemoteException {
    public RemoteCreateAccountExists() {
        super(Color.RED + "[ An account with this username already exists! ]" + Color.RESET );
    }
}
