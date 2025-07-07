package lpz.moonvs.domain.auth.validation;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.auth.entity.UserSchema;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.validation.Validator;

import java.util.regex.Pattern;

public class UserValidator implements Validator<User> {
    public static final String INVALID_CHARACTERS = "error.user.username.invalid-characters";

    private static final int MINIMUM_USERNAME_LENGTH = 4;
    private static final Pattern NON_LETTERS = Pattern.compile("\\W");

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
            this.handler.addError(
                    Notification.nullOrBlank(UserSchema.USERNAME)
            );
            return;
        }

        if (username.length() < MINIMUM_USERNAME_LENGTH)
            this.handler.addError(
                    Notification.minLength(UserSchema.USERNAME,
                            UserSchema.USERNAME, MINIMUM_USERNAME_LENGTH
                    )
            );

        if (NON_LETTERS.matcher(username).find())
            handler.addError(Notification.of(
                    UserSchema.USERNAME,
                    INVALID_CHARACTERS
            ));
    }
}