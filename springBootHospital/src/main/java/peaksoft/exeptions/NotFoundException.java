package peaksoft.exeptions;

/**
 * @author kurstan
 * @created at 23.02.2023 12:51
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }
}
