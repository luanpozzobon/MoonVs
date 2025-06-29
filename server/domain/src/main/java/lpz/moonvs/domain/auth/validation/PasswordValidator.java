package lpz.moonvs.domain.auth.validation;

import lpz.moonvs.domain.auth.valueobject.Password;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.validation.Validator;

import java.util.regex.Pattern;

public class PasswordValidator implements Validator<Password> {
    private final static int MINIMUM_PASSWORD_LENGTH = 8;

    private final static Pattern UPPERCASE_LETTERS = Pattern.compile("[A-Z]");
    private final static Pattern LOWERCASE_LETTERS = Pattern.compile("[a-z]");
    private final static Pattern NUMERIC_CHARS = Pattern.compile("\\d");
    private final static Pattern SPECIAL_CHARS = Pattern.compile("[!@#$%^&*()_+{}\\[\\]:;<>,.?~\\\\/-]");

    private final static String PASSWORD_ERROR_KEY = "password";
    private final static String PASSWORD_ERROR_MESSAGE = "The password must include at least %s.";

    private final NotificationHandler handler;

    public PasswordValidator(final NotificationHandler handler) {
        this.handler = handler;
    }

    @Override
    public void validate(final Password domain) {
        if (domain.getRaw() == null || domain.getRaw().isBlank()) {
            this.handler.addError(new Notification(
                    PASSWORD_ERROR_KEY,
                    "The password must be filled in."
            ));
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
            this.handler.addError(new Notification(
                    PASSWORD_ERROR_KEY,
                    String.format("The password must include at least %d characters.", MINIMUM_PASSWORD_LENGTH)));
    }

    private void validateUppercase(final String password) {
        if (!UPPERCASE_LETTERS.matcher(password).find())
            this.handler.addError(new Notification(
                    PASSWORD_ERROR_KEY,
                    String.format(PASSWORD_ERROR_MESSAGE, "1 uppercase letter")));
    }

    private void validateLowercase(final String password) {
        if (!LOWERCASE_LETTERS.matcher(password).find())
            this.handler.addError(new Notification(
                    PASSWORD_ERROR_KEY,
                    String.format(PASSWORD_ERROR_MESSAGE, "1 lowercase letter")));
    }

    private void validateNumeric(final String password) {
        if (!NUMERIC_CHARS.matcher(password).find())
            this.handler.addError(new Notification(
                    PASSWORD_ERROR_KEY,
                    String.format(PASSWORD_ERROR_MESSAGE, "1 numeric character")));
    }

    private void validateSpecial(final String password) {
        if (!SPECIAL_CHARS.matcher(password).find())
            this.handler.addError(new Notification(
                    PASSWORD_ERROR_KEY,
                    String.format(PASSWORD_ERROR_MESSAGE, "1 special character")));
    }
}
