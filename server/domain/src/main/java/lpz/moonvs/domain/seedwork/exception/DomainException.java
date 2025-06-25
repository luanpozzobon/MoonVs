package lpz.moonvs.domain.seedwork.exception;

import lpz.moonvs.domain.seedwork.notification.Notification;

import java.util.List;

public abstract class DomainException extends RuntimeException {
    private final List<Notification> errors;

    public DomainException(String message, List<Notification> errors) {
        super(message);
        this.errors = errors;
    }

    public List<Notification> getErrors() {
        return errors;
    }
}