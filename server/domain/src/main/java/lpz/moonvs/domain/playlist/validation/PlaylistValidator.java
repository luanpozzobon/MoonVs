package lpz.moonvs.domain.playlist.validation;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.validation.Validator;
import lpz.moonvs.domain.seedwork.valueobject.Id;

public class PlaylistValidator implements Validator<Playlist> {
    private static final int TITLE_MAXIMUM_LENGTH = 64;
    private static final int DESCRIPTION_MAXIMUM_LENGTH = 255;

    private final NotificationHandler handler;

    public PlaylistValidator(final NotificationHandler handler) {
        this.handler = handler;
    }

    @Override
    public void validate(final Playlist domain) {
        this.validateTitle(domain.getTitle());
        this.validateDescription(domain.getDescription());
    }

    private void validateTitle(final String title) {
        if (title == null || title.isBlank()) {
            this.handler.addError(Notification.nullOrBlank(
                    Playlist.Schema.TITLE
            ));
            return;
        }

        if (title.length() > TITLE_MAXIMUM_LENGTH)
            this.handler.addError(Notification.maxLength(
                    Playlist.Schema.TITLE,
                    Playlist.Schema.TITLE, TITLE_MAXIMUM_LENGTH
            ));
    }

    private void validateDescription(final String description) {
        if (description == null || description.isBlank()) return;

        if (description.length() > DESCRIPTION_MAXIMUM_LENGTH)
            this.handler.addError(Notification.maxLength(
                    Playlist.Schema.DESCRIPTION,
                    Playlist.Schema.DESCRIPTION, DESCRIPTION_MAXIMUM_LENGTH
            ));
    }
}
