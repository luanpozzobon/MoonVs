package lpz.moonvs.api.playlist.input;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.seedwork.valueobject.Id;

public record GetAllPlaylistsInput(Id<User> userId) {
}
