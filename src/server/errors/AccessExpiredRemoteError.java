package server.errors;

import utils.Color;
import java.rmi.RemoteException;

public class AccessExpiredRemoteError extends RemoteException {

    public AccessExpiredRemoteError() {
        super(Color.RED + "[ Your access has expired, please login again! ]" + Color.RESET);
    }
}
