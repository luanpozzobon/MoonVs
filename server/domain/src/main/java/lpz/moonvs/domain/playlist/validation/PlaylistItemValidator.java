package lpz.moonvs.domain.playlist.validation;

import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.entity.PlaylistItem;
import lpz.moonvs.domain.playlist.entity.PlaylistItemSchema;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.validation.Validator;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Title;

public class PlaylistItemValidator implements Validator<PlaylistItem> {
    public static final String INVALID_TYPE = "error.playlist.item.type.invalid";

    private final NotificationHandler handler;

    public PlaylistItemValidator(final NotificationHandler handler) {
        this.handler = handler;
    }

    @Override
    public void validate(final PlaylistItem domain) {
        this.validatePlaylistId(domain.getPlaylistId());
        this.validateTitleId(domain.getTitleId());
        this.validateType(domain.getType());
    }

    private void validatePlaylistId(final Id<Playlist> playlistId) {
        if (playlistId == null) {
            this.handler.addError(Notification.nullOrBlank(
                    PlaylistItemSchema.PLAYLIST_ID
            ));
        }
    }

    private void validateTitleId(final Id<Title> titleId) {
        if (titleId == null) {
            this.handler.addError(Notification.nullOrBlank(
                    PlaylistItemSchema.TITLE_ID
            ));
        }
    }

    private void validateType(final String type) {
        if (!"TV".equalsIgnoreCase(type) && !"MOVIE".equalsIgnoreCase(type)) {
            this.handler.addError(Notification.of(
                    PlaylistItemSchema.TYPE, INVALID_TYPE,
                    PlaylistItemSchema.TYPE
            ));
        }
    }
}
