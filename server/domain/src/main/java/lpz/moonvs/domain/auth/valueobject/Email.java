package lpz.moonvs.domain.auth.valueobject;

import lpz.moonvs.domain.auth.validation.EmailValidator;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;

public class Email {
    private final String value;

    private Email(final String value) {
        this.value = value;
    }

    public static Email create(final NotificationHandler handler, final String value) {
        return new Email(value)
                .selfValidate(handler);
    }

    public static Email load(final String value) {
        return new Email(value);
    }

    private Email selfValidate(final NotificationHandler handler) {
        new EmailValidator(handler).validate(this);

        return this;
    }

    public String getValue() {
        return this.value;
    }
}
