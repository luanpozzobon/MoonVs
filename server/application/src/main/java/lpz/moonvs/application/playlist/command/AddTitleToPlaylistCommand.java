package lpz.moonvs.application.playlist.command;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Title;

public record AddTitleToPlaylistCommand(Id<User> userId,
                                        Id<Playlist> playlistId,
                                        Id<Title> titleId,
                                        String type) {
}
