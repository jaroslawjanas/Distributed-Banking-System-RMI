package client.errors;

import utils.Color;

public class ArgumentError extends Throwable {
    public ArgumentError() {
        super(Color.RED + "[ Incorrect arguments! Please try again or use command " + Color.WHITE + "help" + Color.RED + " to see all commands and their usages ]" + Color.RESET);
    }
}