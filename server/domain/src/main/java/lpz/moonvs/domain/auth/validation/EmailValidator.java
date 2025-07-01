package lpz.moonvs.domain.auth.validation;

import lpz.moonvs.domain.auth.valueobject.Email;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.validation.Validator;

public class EmailValidator implements Validator<Email> {
    public static final String EMAIL_ERROR_KEY = "email";
    public static final String NULL_OR_BLANK_KEY = "error.common.null-or-blank";
    public static final String INVALID_EMAIL_KEY = "error.user.email.invalid";

    private final NotificationHandler handler;

    public EmailValidator(final NotificationHandler handler) {
        this.handler = handler;
    }

    @Override
    public void validate(final Email domain) {
        if (domain.getValue() == null || domain.getValue().isBlank()) {
            handler.addError(new Notification(
                    EMAIL_ERROR_KEY,
                    NULL_OR_BLANK_KEY,
                    "E-mail"));
            return;
        }

        if (!domain.getValue().matches("^[\\w.-]+@[\\w.-]+\\.\\w+$"))
            handler.addError(new Notification(
                    EMAIL_ERROR_KEY,
                    INVALID_EMAIL_KEY));
    }
}
