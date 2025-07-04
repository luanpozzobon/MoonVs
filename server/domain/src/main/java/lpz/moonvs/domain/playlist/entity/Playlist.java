package lpz.moonvs.domain.playlist.entity;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.validation.PlaylistValidator;
import lpz.moonvs.domain.seedwork.exception.DomainValidationException;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.valueobject.Id;

public class Playlist {
    public static final String RESOURCE_KEY = "playlist";
    public static final String USER_ID_KEY = "user_id";
    public static final String TITLE_KEY = "title";
    public static final String DESCRIPTION_KEY = "description";

    private final Id<Playlist> id;
    private final Id<User> userId;
    private String title;
    private String description;

    public Id<Playlist> getId() {
        return this.id;
    }

    public Id<User> getUserId() {
        return this.userId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    private Playlist(final NotificationHandler handler,
                     final Id<Playlist> id,
                     final Id<User> userId,
                     final String title,
                     final String description) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;

        this.selfValidate(handler);
    }

    public static Playlist create(final NotificationHandler handler,
                                  final Id<User> userId,
                                  final String title,
                                  final String description) {
        return new Playlist(handler, Id.unique(), userId, title, description);
    }

    public static Playlist load(final Id<Playlist> id,
                                final Id<User> userId,
                                final String title,
                                final String description) {
        return new Playlist(null, id, userId, title, description);
    }

    public void rename(final NotificationHandler handler, final String title) {
        this.title = title;
        this.selfValidate(handler);
    }

    public void updateDescription(final NotificationHandler handler, final String description) {
        this.description = description;
        this.selfValidate(handler);
    }

    private void selfValidate(final NotificationHandler handler) {
        if (handler == null) return;

        new PlaylistValidator(handler).validate(this);

        if (handler.hasError())
            throw new DomainValidationException(handler.getErrors());
    }
}
