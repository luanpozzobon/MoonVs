package lpz.moonvs.domain.playlist.exception;

import lpz.moonvs.domain.seedwork.exception.DomainException;

public class PlaylistItemNotFoundException extends DomainException {
    public PlaylistItemNotFoundException(String message) {
        super(message, null);
    }
}
