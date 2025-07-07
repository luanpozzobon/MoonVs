package lpz.moonvs.domain.auth.validation;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.auth.entity.UserSchema;
import lpz.moonvs.domain.auth.valueobject.Password;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.validation.Validator;

import java.util.regex.Pattern;

public class PasswordValidator implements Validator<Password> {
    public static final String UPPERCASE = "error.user.password.uppercase";
    public static final String LOWERCASE = "error.user.password.lowercase";
    public static final String NUMERIC = "error.user.password.numeric";
    public static final String SPECIAL = "error.user.password.special";

    private static final int MINIMUM_PASSWORD_LENGTH = 8;
    private static final int MINIMUM_UPPERCASE = 1;
    private static final int MINIMUM_LOWERCASE = 1;
    private static final int MINIMUM_NUMERIC = 1;
    private static final int MINIMUM_SPECIAL = 1;

    private static final Pattern UPPERCASE_LETTERS = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_LETTERS = Pattern.compile("[a-z]");
    private static final Pattern NUMERIC_CHARS = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHARS = Pattern.compile("[!@#$%^&*()_+{}\\[\\]:;<>,.?~\\\\/-]");

    private final NotificationHandler handler;

    public PasswordValidator(final NotificationHandler handler) {
        this.handler = handler;
    }

    @Override
    public void validate(final Password domain) {
        if (domain.getRaw() == null || domain.getRaw().isBlank()) {
            this.handler.addError(
                    Notification.nullOrBlank(UserSchema.PASSWORD)
            );
            return;
        }

        this.validateLength(domain.getRaw());
        this.validateUppercase(domain.getRaw());
        this.validateLowercase(domain.getRaw());
        this.validateNumeric(domain.getRaw());
        this.validateSpecial(domain.getRaw());
    }

    private void validateLength(final String password) {
        if (password.length() < MINIMUM_PASSWORD_LENGTH)
            this.handler.addError(Notification.minLength(
                    UserSchema.PASSWORD,
                    UserSchema.PASSWORD, MINIMUM_PASSWORD_LENGTH
            ));
    }

    private void validateUppercase(final String password) {
        if (!UPPERCASE_LETTERS.matcher(password).find())
            this.handler.addError(Notification.of(
                    UserSchema.PASSWORD,
                    UPPERCASE, MINIMUM_UPPERCASE
            ));
    }

    private void validateLowercase(final String password) {
        if (!LOWERCASE_LETTERS.matcher(password).find())
            this.handler.addError(Notification.of(
                    UserSchema.PASSWORD,
                    LOWERCASE, MINIMUM_LOWERCASE
            ));
    }

    private void validateNumeric(final String password) {
        if (!NUMERIC_CHARS.matcher(password).find())
            this.handler.addError(Notification.of(
                    UserSchema.PASSWORD,
                    NUMERIC, MINIMUM_NUMERIC
            ));
    }

    private void validateSpecial(final String password) {
        if (!SPECIAL_CHARS.matcher(password).find())
            this.handler.addError(Notification.of(
                    UserSchema.PASSWORD,
                    SPECIAL, MINIMUM_SPECIAL
            ));
    }
}