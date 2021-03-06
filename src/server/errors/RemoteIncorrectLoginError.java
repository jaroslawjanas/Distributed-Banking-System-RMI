package server.errors;

import utils.Color;
import java.rmi.RemoteException;

public class RemoteIncorrectLoginError extends RemoteException  {
    public RemoteIncorrectLoginError(){
        super(Color.RED + "[ Incorrect login credentials! ]" + Color.RESET);
    }
}
