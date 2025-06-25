package lpz.moonvs.application.playlist.command;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.seedwork.valueobject.Id;

public record UpdatePlaylistCommand(Id<User> userId,
                                    Id<Playlist> id,
                                    String title,
                                    String description) {
}
