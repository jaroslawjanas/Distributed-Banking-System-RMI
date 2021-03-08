package client.errors;

import utils.Color;

public class NumberError extends Throwable {
    public NumberError() {
        super(Color.RED + "[ The amount must be a number! ]" + Color.RESET);
    }
}
