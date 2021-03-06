package client.errors;

import utils.Color;

public class CommandError extends Throwable {
    public CommandError() {
        super(Color.RED + "[ Unrecognised command! ]" + Color.RESET);
    }
}
