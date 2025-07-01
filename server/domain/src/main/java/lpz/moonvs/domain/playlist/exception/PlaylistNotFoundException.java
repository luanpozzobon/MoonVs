package lpz.moonvs.domain.playlist.exception;

import lpz.moonvs.domain.seedwork.exception.DomainException;

public class PlaylistNotFoundException extends DomainException {
    public static final String ERROR_KEY = "error.playlist.not-found";

    public PlaylistNotFoundException() {
        super(ERROR_KEY, null);
    }
}
