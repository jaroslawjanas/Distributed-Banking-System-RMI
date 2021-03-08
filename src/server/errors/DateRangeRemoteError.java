package server.errors;

import utils.Color;
import java.rmi.RemoteException;

public class DateRangeRemoteError extends RemoteException {

    public DateRangeRemoteError() {
        super(Color.RED + "[ Incorrect date range ! ]");
    }
}
