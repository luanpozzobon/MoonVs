package lpz.moonvs.domain.auth.validation;

import lpz.moonvs.domain.auth.entity.UserSchema;
import lpz.moonvs.domain.auth.valueobject.Email;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.validation.Validator;

public class EmailValidator implements Validator<Email> {
    public static final String INVALID = "error.user.email.invalid";

    private final NotificationHandler handler;

    public EmailValidator(final NotificationHandler handler) {
        this.handler = handler;
    }

    @Override
    public void validate(final Email domain) {
        if (domain.getValue() == null || domain.getValue().isBlank()) {
            this.handler.addError(
                    Notification.nullOrBlank(UserSchema.EMAIL, UserSchema.EMAIL)
            );

            return;
        }

        if (!domain.getValue().matches("^[\\w.-]+@[\\w.-]+\\.\\w+$"))
            this.handler.addError(Notification.of(
                    UserSchema.EMAIL,
                    INVALID
            ));
    }
}
