package lpz.moonvs.application.playlist.output;

import lpz.moonvs.domain.playlist.entity.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

public record GetAllTitlesFromPlaylistOutput(List<Title> titles,
                                             Metadata metadata) {
    public static GetAllTitlesFromPlaylistOutput from(final List<PlaylistItem> playlistItems,
                                                      final Integer page,
                                                      final Integer totalPages) {
        final var output = new GetAllTitlesFromPlaylistOutput(new ArrayList<>(), new Metadata(page, totalPages));
        playlistItems.forEach(item -> output.titles().add(
                new Title(Long.parseLong(item.getTitleId().getValue()), item.getType()))
        );

        return output;
    }

    public record Title(Long id,
                        String type) {
    }

    public record Metadata(Integer page,
                           Integer totalPages) {
    }
}


