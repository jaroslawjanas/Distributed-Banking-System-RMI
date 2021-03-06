package client.errors;

import utils.Color;

public class ArgumentError extends Throwable {
    public ArgumentError() {
        super(Color.RED + "[ Incorrect arguments! ]" + Color.RESET);
    }
}
