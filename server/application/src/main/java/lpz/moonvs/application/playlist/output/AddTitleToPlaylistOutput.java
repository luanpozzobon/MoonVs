package lpz.moonvs.application.playlist.output;

import lpz.moonvs.domain.playlist.entity.PlaylistItem;

public record AddTitleToPlaylistOutput(String playlistId,
                                       String titleId) {
    public static AddTitleToPlaylistOutput from(final PlaylistItem playlistItem) {
        return new AddTitleToPlaylistOutput(playlistItem.getPlaylistId().getValue(), playlistItem.getTitleId().getValue());
    }
}
