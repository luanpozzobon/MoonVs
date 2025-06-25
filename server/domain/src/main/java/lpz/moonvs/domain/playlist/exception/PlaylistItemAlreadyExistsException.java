package lpz.moonvs.domain.playlist.exception;

public class PlaylistItemAlreadyExistsException extends RuntimeException {
    public PlaylistItemAlreadyExistsException(String message) {
        super(message);
    }
}
