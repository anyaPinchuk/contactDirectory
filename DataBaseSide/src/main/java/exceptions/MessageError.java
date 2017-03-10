package exceptions;

public class MessageError {
    private String message;
    private Exception exception;

    public MessageError(String message, Exception exception) {
        this.message = message;
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public Exception getException() {
        return exception;
    }
}
