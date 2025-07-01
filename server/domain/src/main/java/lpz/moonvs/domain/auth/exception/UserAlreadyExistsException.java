package lpz.moonvs.domain.auth.exception;

import lpz.moonvs.domain.seedwork.exception.DomainException;
import lpz.moonvs.domain.seedwork.notification.Notification;

import java.util.List;

public class UserAlreadyExistsException extends DomainException {
    public final static String ERROR_KEY = "error.user.already-exists";

    public UserAlreadyExistsException(List<Notification> errors) {
        super(ERROR_KEY, errors);
    }
}
