package client.errors;

import utils.Color;

public class DateFormatError extends Throwable{

    public DateFormatError() {
        super(Color.RED + "[ Incorrect date format! The date must be in \"dd/MM/yyyy\" format. ]" + Color.RESET);
    }
}
