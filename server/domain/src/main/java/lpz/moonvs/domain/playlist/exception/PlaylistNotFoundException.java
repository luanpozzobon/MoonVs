package lpz.moonvs.domain.playlist.exception;

import lpz.moonvs.domain.seedwork.exception.DomainException;

public class PlaylistNotFoundException extends DomainException {
    public PlaylistNotFoundException(String message) {
        super(message, null);
    }
}
