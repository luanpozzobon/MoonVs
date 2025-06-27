package lpz.moonvs.domain.auth.valueobject;

import lpz.moonvs.domain.auth.contracts.IPasswordEncryptor;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.auth.validation.PasswordValidator;

public class Password  {
    private String raw;
    private String encrypted;
    private IPasswordEncryptor encryptor;

    private Password(final IPasswordEncryptor encryptor,
                     final NotificationHandler handler,
                     final String raw) {
        this.raw = raw;
        this.selfValidate(handler);

        if (handler.hasError())
            return;

        this.encryptor = encryptor;
        this.encrypted = this.encryptor.encrypt(this.raw);
    }

    private Password(final String encrypted) {
        this.encrypted = encrypted;
    }

    private void selfValidate(final NotificationHandler handler) {
        new PasswordValidator(handler).validate(this);
    }

    public static Password create(final IPasswordEncryptor encryptor,
                                  final NotificationHandler handler,
                                  final String raw) {
        return new Password(encryptor, handler, raw);
    }

    public static Password encrypted(final String encrypted) {
        return new Password(encrypted);
    }

    public String getRaw() {
        return this.raw;
    }

    public String getValue() {
        return this.encrypted;
    }
}
