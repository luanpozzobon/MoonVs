package lpz.moonvs.domain.title.validation;

import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.validation.Validator;
import lpz.moonvs.domain.title.entity.Title;
import lpz.moonvs.domain.title.entity.schema.TitleSchema;

public class TitleValidator implements Validator<Title> {
    public static final String INVALID_TMDB_ID = "error.title.tmdb-id.invalid";
    public static final String INVALID_SCREEN_TIME = "error.title.screen-time.invalid";

    private static final int MIN_TMDB_ID = 1;
    private static final int MIN_SCREEN_TIME = 1;

    private final NotificationHandler handler;

    public TitleValidator(final NotificationHandler handler) {
        this.handler = handler;
    }

    @Override
    public void validate(Title domain) {
        validateTmdbId(domain.getTmdbId());
        validateTitle(domain.getTitle());
        validateScreenTime(domain.getScreenTime());
    }

    private void validateTmdbId(final Integer tmdbId) {
        if (tmdbId < MIN_TMDB_ID)
            handler.addError(Notification.of(
                    TitleSchema.TMDB_ID,
                    INVALID_TMDB_ID, tmdbId
            ));
    }

    private void validateTitle(final String title) {
        if (title.isBlank())
            handler.addError(Notification.nullOrBlank(
                    TitleSchema.TITLE
            ));
    }

    private void validateScreenTime(final Integer screenTime) {
        if (screenTime != null && screenTime < MIN_SCREEN_TIME)
            handler.addError(Notification.of(
                    TitleSchema.SCREEN_TIME,
                    INVALID_SCREEN_TIME, screenTime
            ));
    }
}
