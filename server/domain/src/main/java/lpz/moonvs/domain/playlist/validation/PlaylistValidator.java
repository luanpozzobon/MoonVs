package lpz.moonvs.domain.playlist.validation;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.validation.Validator;
import lpz.moonvs.domain.seedwork.valueobject.Id;

public class PlaylistValidator implements Validator<Playlist> {
    private final NotificationHandler handler;

    private static final String USER_ID_ERROR_KEY = "user_id";
    private static final String USER_ID_NULL_MESSAGE = "The user id cannot be null.";

    private static final int TITLE_MAXIMUM_LENGTH = 64;
    private static final String TITLE_ERROR_KEY = "title";
    private static final String TITLE_NULL_MESSAGE = "The title cannot be null or blank.";
    private static final String TITLE_MAXIMUM_LENGTH_MESSAGE = "The title length must not be bigger than %d characters.";

    private static final int DESCRIPTION_MAXIMUM_LENGTH = 255;
    private static final String DESCRIPTION_ERROR_KEY = "description";
    private static final String DESCRIPTION_MAXIMUM_LENGTH_MESSAGE = "The description length must not be bigger than %d characters.";

    public PlaylistValidator(final NotificationHandler handler) {
        this.handler = handler;
    }

    @Override
    public void validate(final Playlist domain) {
        this.validateUserId(domain.getUserId());
        this.validateTitle(domain.getTitle());
        this.validateDescription(domain.getDescription());
    }

    private void validateUserId(final Id<User> userId) {
        if (userId == null)
            this.handler.addError(new Notification(
                    USER_ID_ERROR_KEY,
                    USER_ID_NULL_MESSAGE
            ));
    }

    private void validateTitle(final String title) {
        if (title == null  || title.isBlank()) {
            this.handler.addError(new Notification(
                    TITLE_ERROR_KEY,
                    TITLE_NULL_MESSAGE
            ));
            return;
        }

        if (title.length() > TITLE_MAXIMUM_LENGTH)
            this.handler.addError(new Notification(
                    TITLE_ERROR_KEY,
                    String.format(TITLE_MAXIMUM_LENGTH_MESSAGE, TITLE_MAXIMUM_LENGTH)
            ));
    }

    private void validateDescription(final String description) {
        if (description == null || description.isBlank()) return;

        if (description.length() > DESCRIPTION_MAXIMUM_LENGTH)
            this.handler.addError(new Notification(
                    DESCRIPTION_ERROR_KEY,
                    String.format(DESCRIPTION_MAXIMUM_LENGTH_MESSAGE, DESCRIPTION_MAXIMUM_LENGTH)
            ));
    }
}
