package lpz.moonvs.domain.playlist.exception;

import lpz.moonvs.domain.seedwork.exception.DomainException;

public class PlaylistItemNotFoundException extends DomainException {
    public final static String ERROR_KEY = "error.playlist.item.not-found";

    public PlaylistItemNotFoundException() {
        super(ERROR_KEY, null);
    }
}
