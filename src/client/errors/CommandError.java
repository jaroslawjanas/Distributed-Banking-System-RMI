package client.errors;

import utils.Color;

public class CommandError extends Throwable {
    public CommandError() {
        super(Color.RED + "[ Unrecognised command! Please try again or use command " + Color.WHITE + "help" + Color.RED + " to see all commands and their usages ]" + Color.RESET);
    }
}


