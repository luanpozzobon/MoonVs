package lpz.moonvs.domain.playlist.entity;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.validation.PlaylistValidator;
import lpz.moonvs.domain.seedwork.exception.DomainValidationException;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.valueobject.Id;

import java.util.Objects;

public class Playlist {
    public interface Schema {
        String RESOURCE = "playlist";
        String ID = "id";
        String USER_ID = "user_id";
        String TITLE = "title";
        String DESCRIPTION = "description";
    }

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

    private Playlist(final Id<Playlist> id,
                     final Id<User> userId,
                     final String title,
                     final String description) {
        final String message = "'%s' cannot be null.";
        this.id = Objects.requireNonNull(id, String.format(message, Schema.ID));
        this.userId = Objects.requireNonNull(userId, String.format(message, Schema.USER_ID));
        this.title = Objects.requireNonNull(title, String.format(message, Schema.TITLE));
        this.description = description;
    }

    public static Playlist create(final NotificationHandler handler,
                                  final Id<User> userId,
                                  final String title,
                                  final String description) {
        return new Playlist(Id.unique(), userId, title, description)
                .selfValidate(handler);
    }

    public static Playlist load(final Id<Playlist> id,
                                final Id<User> userId,
                                final String title,
                                final String description) {
        return new Playlist(id, userId, title, description);
    }

    public void rename(final NotificationHandler handler, final String title) {
        this.title = title;
        this.selfValidate(handler);
    }

    public void updateDescription(final NotificationHandler handler, final String description) {
        this.description = description;
        this.selfValidate(handler);
    }

    private Playlist selfValidate(final NotificationHandler handler) {
        new PlaylistValidator(handler).validate(this);

        if (handler.hasError())
            throw new DomainValidationException(handler.getErrors());

        return this;
    }
}
