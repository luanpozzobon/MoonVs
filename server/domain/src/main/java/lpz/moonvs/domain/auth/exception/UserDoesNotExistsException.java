package lpz.moonvs.domain.auth.exception;

import lpz.moonvs.domain.seedwork.exception.DomainException;

public class UserDoesNotExistsException extends DomainException {
    public UserDoesNotExistsException(String message) {
        super(message, null);
    }
}
