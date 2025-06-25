package lpz.moonvs.application.playlist.command;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.seedwork.valueobject.Id;

public record GetAllTitlesFromPlaylistCommand(Id<User> userId,
                                              Id<Playlist> playlistId,
                                              Integer page) {
}
