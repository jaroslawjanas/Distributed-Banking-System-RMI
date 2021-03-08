package client.errors;

import utils.Color;

public class NotLoggedInError extends Throwable {
    public NotLoggedInError(){
        super(Color.RED + "[ You are not logged in! Please try again or use command " + Color.WHITE + "help" + Color.RED + " to see all commands and their usages ]" + Color.RESET);
    }

}
