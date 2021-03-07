package server.errors;

import utils.Color;
import java.rmi.RemoteException;

public class AccountNotFoundRemoteError extends RemoteException {

    public AccountNotFoundRemoteError() {
        super(Color.RED + "[ Could not find the account ! ]" + Color.RESET);
    }
}
