package server.errors;

import utils.Color;
import java.rmi.RemoteException;

public class CreateAccountExistsRemoteError extends RemoteException {

    public CreateAccountExistsRemoteError() {
        super(Color.RED + "[ An account with this username already exists! ]" + Color.RESET );
    }
}
