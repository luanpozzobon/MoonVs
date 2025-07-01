package lpz.moonvs.domain.playlist.entity;

import lpz.moonvs.domain.playlist.validation.PlaylistItemValidator;
import lpz.moonvs.domain.seedwork.exception.DomainValidationException;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Title;

public class PlaylistItem {
    public static final String RESOURCE_KEY = "playlist_item";
    public static final String PLAYLIST_ID_KEY = "playlist_id";
    public static final String TITLE_ID_KEY = "title_id";
    public static final String TYPE_KEY = "type";

    private final Id<Playlist> playlistId;
    private final Id<Title> titleId;
    private final String type;

    public Id<Playlist> getPlaylistId() {
        return this.playlistId;
    }

    public Id<Title> getTitleId() {
        return this.titleId;
    }

    public String getType() {
        return type;
    }

    private PlaylistItem(final NotificationHandler handler,
                         final Id<Playlist> playlistId,
                         final Id<Title> titleId,
                         final String type) {
        this.playlistId = playlistId;
        this.titleId = titleId;
        this.type = type;

        this.selfValidate(handler);
    }

    public static PlaylistItem create(final NotificationHandler handler,
                                      final Id<Playlist> playlistId,
                                      final Id<Title> titleId,
                                      final String type) {
        return new PlaylistItem(handler, playlistId, titleId, type);
    }

    public static PlaylistItem load(final Id<Playlist> playlistId,
                                    final Id<Title> titleId,
                                    final String type) {
        return new PlaylistItem(null, playlistId, titleId, type);
    }

    private void selfValidate(final NotificationHandler handler) {
        if (handler == null) return;

        new PlaylistItemValidator(handler).validate(this);

        if (handler.hasError())
            throw new DomainValidationException(handler.getErrors());
    }
}
