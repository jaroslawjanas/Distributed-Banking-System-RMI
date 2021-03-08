package server.errors;

import utils.Color;
import java.rmi.RemoteException;

public class InputRemoteError extends RemoteException {

    public InputRemoteError(){
        super(Color.RED + "[ Invalid input, please input a positive number up to two decimal places and try again! ]" + Color.RESET);
    }
}

