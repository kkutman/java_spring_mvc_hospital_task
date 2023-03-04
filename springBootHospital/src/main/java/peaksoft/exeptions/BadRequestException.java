package peaksoft.exeptions;

/**
 * @author kurstan
 * @created at 03.03.2023 20:23
 */
public class BadRequestException extends RuntimeException{
    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }
}
