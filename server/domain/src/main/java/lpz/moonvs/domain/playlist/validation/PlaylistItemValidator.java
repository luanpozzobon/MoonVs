package lpz.moonvs.domain.playlist.validation;

import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.entity.PlaylistItem;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.validation.Validator;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Title;

public class PlaylistItemValidator implements Validator<PlaylistItem> {
    public static final String NULL_OR_BLANK_KEY = "error.common.null-or-blank";
    public static final String INVALID_TYPE_KEY = "error.playlist.item.type.invalid";

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
            handler.addError(new Notification(
                    PlaylistItem.PLAYLIST_ID_KEY,
                    NULL_OR_BLANK_KEY, PlaylistItem.PLAYLIST_ID_KEY
            ));
        }
    }

    private void validateTitleId(final Id<Title> titleId) {
        if (titleId == null) {
            handler.addError(new Notification(
                    PlaylistItem.TITLE_ID_KEY,
                    NULL_OR_BLANK_KEY, PlaylistItem.TITLE_ID_KEY
            ));
        }
    }

    private void validateType(final String type) {
        if (!"TV".equalsIgnoreCase(type) && !"MOVIE".equalsIgnoreCase(type)) {
            handler.addError(new Notification(
                    PlaylistItem.TYPE_KEY,
                    INVALID_TYPE_KEY, PlaylistItem.TYPE_KEY
            ));
        }
    }
}
