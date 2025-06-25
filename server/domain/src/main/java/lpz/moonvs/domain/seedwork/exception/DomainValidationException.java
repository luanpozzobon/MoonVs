package lpz.moonvs.domain.seedwork.exception;

import lpz.moonvs.domain.seedwork.notification.Notification;

import java.util.List;

public class DomainValidationException extends DomainException {
    public DomainValidationException(final String message, final List<Notification> errors) {
        super(message, errors);
    }
}