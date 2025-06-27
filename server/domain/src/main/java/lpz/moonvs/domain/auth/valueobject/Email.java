package lpz.moonvs.domain.auth.valueobject;

import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.auth.validation.EmailValidator;

public class Email {
    private final String value;

    private Email(final NotificationHandler handler,
                  final String value) {
        this.value = value;
        this.selfValidate(handler);
    }

    private Email(final String value) {
        this.value = value;
    }

    public static Email create(final NotificationHandler handler, final String value) {
        return new Email(handler, value);
    }

    public static Email load(final String value) {
        return new Email(value);
    }

    private void selfValidate(final NotificationHandler handler) {
        new EmailValidator(handler).validate(this);
    }

    public String getValue() {
        return this.value;
    }
}
