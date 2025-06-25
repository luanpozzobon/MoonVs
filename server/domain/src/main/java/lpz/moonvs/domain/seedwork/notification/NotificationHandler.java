package lpz.moonvs.domain.seedwork.notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationHandler {
    private final List<Notification> errors;

    private NotificationHandler(final List<Notification> errors) {
        this.errors = errors;
    }

    public static NotificationHandler create() {
        return new NotificationHandler(new ArrayList<>());
    }

    public static NotificationHandler create(final List<Notification> errors) {
        return new NotificationHandler(errors);
    }

    public void addError(final Notification error) {
        this.errors.add(error);
    }

    public void addErrors(final List<Notification> errors) {
        this.errors.addAll(errors);
    }

    public boolean hasError() {
        return !this.errors.isEmpty();
    }

    public List<Notification> getErrors() {
        return this.errors;
    }
}
