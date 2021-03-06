package server.errors;

import utils.Color;

public class InputError extends Throwable {
    public InputError(){
        super(Color.RED + "Invalid input: please input a positive number and try again." + Color.RESET);
    }
}

