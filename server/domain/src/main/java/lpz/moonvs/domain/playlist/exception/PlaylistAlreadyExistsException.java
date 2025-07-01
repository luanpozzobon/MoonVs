package lpz.moonvs.domain.playlist.exception;

import lpz.moonvs.domain.seedwork.exception.DomainException;
import lpz.moonvs.domain.seedwork.notification.Notification;

import java.util.List;

public class PlaylistAlreadyExistsException extends DomainException {
    public final static String ERROR_KEY = "error.playlist.already-exists";

    public PlaylistAlreadyExistsException(List<Notification> errors) {
        super(ERROR_KEY, errors);
    }
}