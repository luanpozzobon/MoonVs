package lpz.moonvs.domain.title.exception;

import lpz.moonvs.domain.seedwork.exception.DomainException;

public class TitleNotFoundException extends DomainException {
    public static final String ERROR_KEY = "error.title.not-found";

    public TitleNotFoundException() {
        super(ERROR_KEY, null);
    }
}
