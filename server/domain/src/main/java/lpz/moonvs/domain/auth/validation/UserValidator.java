package lpz.moonvs.domain.auth.validation;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.validation.Validator;

import java.util.regex.Pattern;

public class UserValidator implements Validator<User> {
    private final static int MINIMUM_USERNAME_LENGTH = 4;

    private final static Pattern NON_LETTERS = Pattern.compile("\\W");

    public final static String NULL_OR_BLANK_KEY = "error.common.null-or-blank";
    public final static String MINIMUM_LENGTH_KEY = "error.common.min-length";
    public final static String INVALID_CHARACTERS_KEY = "error.user.username.invalid-characters";

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
                    User.USERNAME_KEY,
                    NULL_OR_BLANK_KEY, "Username"));
            return;
        }

        if (username.length() < MINIMUM_USERNAME_LENGTH)
            handler.addError(new Notification(
                    User.USERNAME_KEY,
                    MINIMUM_LENGTH_KEY,
                    "Username", MINIMUM_USERNAME_LENGTH
            ));

        if (NON_LETTERS.matcher(username).find())
            handler.addError(new Notification(
                    User.USERNAME_KEY,
                    INVALID_CHARACTERS_KEY
            ));
    }
}