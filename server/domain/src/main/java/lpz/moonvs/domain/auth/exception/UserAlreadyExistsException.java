package lpz.moonvs.domain.auth.exception;

import lpz.moonvs.domain.seedwork.exception.DomainException;
import lpz.moonvs.domain.seedwork.notification.Notification;

import java.util.List;

public class UserAlreadyExistsException extends DomainException {
    public UserAlreadyExistsException(String message, List<Notification> errors) {
        super(message, errors);
    }
}
