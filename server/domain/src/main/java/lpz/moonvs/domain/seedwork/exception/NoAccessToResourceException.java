package lpz.moonvs.domain.seedwork.exception;

public class NoAccessToResourceException extends DomainException {
    public final static String ERROR_KEY = "error.common.no-access";

    public NoAccessToResourceException() {
        super(ERROR_KEY, null);
    }

    public NoAccessToResourceException(final String message) {
        super(message, null);
    }
}
