package server.errors;

import utils.Color;
import java.rmi.RemoteException;

public class AccessDeniedRemoteError extends RemoteException {

    public AccessDeniedRemoteError() {
        super(Color.RED + "[ Access denied! ]" + Color.RESET);
    }
}
