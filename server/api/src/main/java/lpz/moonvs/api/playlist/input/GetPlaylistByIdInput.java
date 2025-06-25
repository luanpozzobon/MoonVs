package lpz.moonvs.api.playlist.input;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.seedwork.valueobject.Id;

public record GetPlaylistByIdInput(Id<User> userId,
                                   Id<Playlist> playlistId) {
}
