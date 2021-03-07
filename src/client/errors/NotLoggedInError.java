package client.errors;

import utils.Color;

public class NotLoggedInError extends Throwable {
    public NotLoggedInError(){
        super(Color.RED + "[ You are not logged in! ]" + Color.RESET);
    }

}
