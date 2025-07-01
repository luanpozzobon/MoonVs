package lpz.moonvs.domain.playlist.validation;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.validation.Validator;
import lpz.moonvs.domain.seedwork.valueobject.Id;

public class PlaylistValidator implements Validator<Playlist> {
    public final static String NULL_OR_BLANK_KEY = "error.common.null-or-blank";
    public final static String MAXIMUM_LENGTH_KEY = "error.common.max-length";

    private static final int TITLE_MAXIMUM_LENGTH = 64;
    private static final int DESCRIPTION_MAXIMUM_LENGTH = 255;

    private final NotificationHandler handler;

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
                    Playlist.USER_ID_KEY,
                    NULL_OR_BLANK_KEY, Playlist.USER_ID_KEY
            ));
    }

    private void validateTitle(final String title) {
        if (title == null  || title.isBlank()) {
            this.handler.addError(new Notification(
                    Playlist.TITLE_KEY,
                    NULL_OR_BLANK_KEY, Playlist.TITLE_KEY
            ));
            return;
        }

        if (title.length() > TITLE_MAXIMUM_LENGTH)
            this.handler.addError(new Notification(
                    Playlist.TITLE_KEY,
                    MAXIMUM_LENGTH_KEY, Playlist.TITLE_KEY, TITLE_MAXIMUM_LENGTH
            ));
    }

    private void validateDescription(final String description) {
        if (description == null || description.isBlank()) return;

        if (description.length() > DESCRIPTION_MAXIMUM_LENGTH)
            this.handler.addError(new Notification(
                    Playlist.DESCRIPTION_KEY,
                    MAXIMUM_LENGTH_KEY, Playlist.DESCRIPTION_KEY, DESCRIPTION_MAXIMUM_LENGTH
            ));
    }
}
