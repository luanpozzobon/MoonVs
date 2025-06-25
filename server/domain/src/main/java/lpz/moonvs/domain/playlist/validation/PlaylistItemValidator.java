package lpz.moonvs.domain.playlist.validation;

import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.entity.PlaylistItem;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.validation.Validator;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Title;

public class PlaylistItemValidator implements Validator<PlaylistItem> {
    private final NotificationHandler handler;

    private static final String PLAYLIST_ID_ERROR_KEY = "playlist_id";
    private static final String PLAYLIST_ID_NULL_MESSAGE = "The playlist id cannot be null or blank.";

    private static final String TITLE_ID_ERROR_KEY = "title_id";
    private static final String TITLE_ID_NULL_MESSAGE = "The title id cannot be null or blank.";

    private static final String TYPE_ERROR_KEY = "type";
    private static final String TYPE_INVALID_MESSAGE = "The type can only be 'TV' or 'MOVIE'";

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
        if (playlistId.getValue() == null || playlistId.getValue().isBlank()) {
            handler.addError(new Notification(
                    PLAYLIST_ID_ERROR_KEY, PLAYLIST_ID_NULL_MESSAGE
            ));
        }
    }

    private void validateTitleId(final Id<Title> titleId) {
        if (titleId.getValue() == null || titleId.getValue().isBlank()) {
            handler.addError(new Notification(
                    TITLE_ID_ERROR_KEY, TITLE_ID_NULL_MESSAGE
            ));
        }
    }

    private void validateType(final String type) {
        if (!"TV".equalsIgnoreCase(type) && !"MOVIE".equalsIgnoreCase(type)) {
            handler.addError(new Notification(
                    TYPE_ERROR_KEY, TYPE_INVALID_MESSAGE
            ));
        }
    }
}
