package lpz.moonvs.domain.auth.valueobject;

import lpz.moonvs.domain.auth.contracts.IPasswordEncryptor;
import lpz.moonvs.domain.auth.validation.PasswordValidator;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;

public class Password  {
    private String raw;
    private String encrypted;

    private Password(final IPasswordEncryptor encryptor,
                     final NotificationHandler handler,
                     final String raw) {
        this.raw = raw;
        new PasswordValidator(handler).validate(this);

        if (!handler.hasError())
            this.encrypted = encryptor.encrypt(raw);

        this.raw = null;
    }

    private Password(final String encrypted) {
        this.encrypted = encrypted;
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
