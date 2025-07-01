package lpz.moonvs.domain.seedwork.exception;

import lpz.moonvs.domain.seedwork.notification.Notification;

import java.util.List;

public class DomainValidationException extends DomainException {
    public static final String ERROR_KEY = "error.common.invalid";

    public DomainValidationException(final List<Notification> errors) {
        super(ERROR_KEY, errors);
    }

    public DomainValidationException(final String message, final List<Notification> errors) {
        super(message, errors);
    }
}