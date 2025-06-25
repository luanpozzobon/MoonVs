package lpz.moonvs.domain.playlist.exception;

import lpz.moonvs.domain.seedwork.exception.DomainException;
import lpz.moonvs.domain.seedwork.notification.Notification;

import java.util.List;

public class PlaylistAlreadyExistsException extends DomainException {
    public PlaylistAlreadyExistsException(String message, List<Notification> errors) {
        super(message, errors);
    }
}