package reliability.exception;

public class InvalidEntityException extends IllegalArgumentException {

    public InvalidEntityException(String message) {
        super(message);
    }
}