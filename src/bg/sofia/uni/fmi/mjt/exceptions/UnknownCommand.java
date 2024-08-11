package bg.sofia.uni.fmi.mjt.exceptions;

public class UnknownCommand extends Exception {
    public UnknownCommand(String message) {
        super(message);
    }

    public UnknownCommand(String message, Throwable cause) {
        super(message, cause);
    }
}
