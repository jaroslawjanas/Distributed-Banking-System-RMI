package server.errors;

import utils.Color;
import java.rmi.RemoteException;

public class IncorrectLoginRemoteError extends RemoteException  {

    public IncorrectLoginRemoteError(){
        super(Color.RED + "[ Incorrect login credentials! Please try again or create a new account with: " + Color.WHITE +"create [username] [password]" + Color.RED +" ]" + Color.RESET);
    }
}
