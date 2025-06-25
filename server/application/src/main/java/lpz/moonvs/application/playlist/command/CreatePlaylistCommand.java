package lpz.moonvs.application.playlist.command;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.seedwork.valueobject.Id;

public record CreatePlaylistCommand(Id<User> userId,
                                    String title,
                                    String description) {
}
