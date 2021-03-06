package server.errors;

import utils.Color;
import java.rmi.RemoteException;

public class IncorrectLoginRemoteError extends RemoteException  {

    public IncorrectLoginRemoteError(){
        super(Color.RED + "[ Incorrect login credentials! ]" + Color.RESET);
    }
}
