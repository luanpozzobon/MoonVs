package lpz.moonvs.domain.auth.validation;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.validation.Validator;

import java.util.regex.Pattern;

public class UserValidator implements Validator<User> {
    private final static int MINIMUM_USERNAME_LENGTH = 4;

    private final static Pattern NON_LETTERS = Pattern.compile("\\W");

    private final static String USERNAME_ERROR_KEY = "username";
    private final static String USERNAME_LENGTH_MESSAGE = "The username must have at least %d characters.";
    private final static String USERNAME_ERROR_MESSAGE = "The username must include only alphanumeric characters";

    private final NotificationHandler handler;

    public UserValidator(final NotificationHandler handler) {
        this.handler = handler;
    }

    @Override
    public void validate(final User user) {
        this.validateUsername(user.getUsername());
    }

    private void validateUsername(final String username) {
        if (username == null || username.isBlank()) {
            this.handler.addError(new Notification(
                    USERNAME_ERROR_KEY,
                    "The username must be filled in."));
            return;
        }

        if (username.length() < MINIMUM_USERNAME_LENGTH)
            handler.addError(new Notification(
                    USERNAME_ERROR_KEY,
                    String.format(USERNAME_LENGTH_MESSAGE, MINIMUM_USERNAME_LENGTH)));

        if (NON_LETTERS.matcher(username).find())
            handler.addError(new Notification(
                    USERNAME_ERROR_KEY,
                    USERNAME_ERROR_MESSAGE));
    }
}