package peaksoft.exeptions;

/**
 * @author kurstan
 * @created at 02.03.2023 16:47
 */
public class ExistsInDataBase extends RuntimeException{
    public ExistsInDataBase() {
        super();
    }

    public ExistsInDataBase(String message) {
        super(message);
    }
}
