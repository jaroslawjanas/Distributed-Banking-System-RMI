package client.errors;

public class CommandError extends Throwable {
    public CommandError() {
        super("[ Unrecognised command! ]");
    }
}
