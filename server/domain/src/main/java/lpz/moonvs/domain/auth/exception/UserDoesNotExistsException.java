package lpz.moonvs.domain.auth.exception;

import lpz.moonvs.domain.seedwork.exception.DomainException;

public class UserDoesNotExistsException extends DomainException {
    public static final String ERROR_KEY = "error.user.not-found";

    public UserDoesNotExistsException() {
        super(ERROR_KEY, null);
    }
}
