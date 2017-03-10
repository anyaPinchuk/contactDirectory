package exceptions;

public class GenericDAOException extends Exception {
    public GenericDAOException(Exception e) {
        super("The exception occurred in DAO", e);
    }

    public GenericDAOException(String message) {
        super(message);
    }

    public GenericDAOException(String message, Exception e) {
        super(message, e);
    }
}
