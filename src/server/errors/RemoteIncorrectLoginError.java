package server.errors;

import utils.Color;

import java.io.Serializable;
import java.rmi.RemoteException;

public class RemoteIncorrectLoginError extends RemoteException implements Serializable {
    public RemoteIncorrectLoginError(){
        super(Color.RED + "[ Incorrect login credentials! ]" + Color.RESET);
    }
}
