package lpz.moonvs.domain.seedwork.exception;

public class NoAccessToResourceException extends DomainException {
    public NoAccessToResourceException(final String message) {
        super(message, null);
    }
}
