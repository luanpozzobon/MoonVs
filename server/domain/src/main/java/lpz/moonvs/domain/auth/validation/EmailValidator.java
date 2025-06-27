package lpz.moonvs.domain.auth.validation;

import lpz.moonvs.domain.auth.valueobject.Email;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.validation.Validator;

public class EmailValidator implements Validator<Email> {
    private static final String EMAIL_ERROR_KEY = "email";

    private final NotificationHandler handler;

    public EmailValidator(final NotificationHandler handler) {
        this.handler = handler;
    }

    @Override
    public void validate(final Email domain) {
        if (domain.getValue() == null || domain.getValue().isBlank()) {
            handler.addError(new Notification(
                    EMAIL_ERROR_KEY,
                    "The E-mail must be filled in."));
            return;
        }

        if (!domain.getValue().matches("^[\\w.-]+@[\\w.-]+\\.\\w+$"))
            handler.addError(new Notification(
                    EMAIL_ERROR_KEY,
                    "This doesn't seem to be a valid E-mail."));
    }
}
