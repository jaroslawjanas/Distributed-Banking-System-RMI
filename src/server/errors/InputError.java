package server.errors;

public class InputError extends Throwable {
    public InputError(){
        super("Invalid input: please input a positive number and try again.");
    }
}

