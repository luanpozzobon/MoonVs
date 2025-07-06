package lpz.moonvs.domain.auth.validation;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.auth.valueobject.Email;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.validation.Validator;

public class EmailValidator implements Validator<Email> {
    public interface Schema {
        String INVALID = "error.user.email.invalid";
    }

    private final NotificationHandler handler;

    public EmailValidator(final NotificationHandler handler) {
        this.handler = handler;
    }

    @Override
    public void validate(final Email domain) {
        if (domain.getValue() == null || domain.getValue().isBlank()) {
            this.handler.addError(
                    Notification.nullOrBlank(User.Schema.EMAIL, User.Schema.EMAIL)
            );

            return;
        }

        if (!domain.getValue().matches("^[\\w.-]+@[\\w.-]+\\.\\w+$"))
            this.handler.addError(Notification.of(
                    User.Schema.EMAIL,
                    Schema.INVALID
            ));
    }
}
