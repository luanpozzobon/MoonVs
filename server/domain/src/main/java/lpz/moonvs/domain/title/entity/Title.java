package lpz.moonvs.domain.title.entity;

import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.valueobject.Id;

public class Title {
    private final Id<Title> id;
    private final Long tmdbId;

    public Id<Title> getId() {
        return id;
    }

    public Long getTmdbId() {
        return tmdbId;
    }

    private Title(final NotificationHandler handler,
                  final Id<Title> id,
                  final Long tmdbId) {
        this.id = id;
        this.tmdbId = tmdbId;

        this.selfValidate(handler);
    }

    public static Title create(final NotificationHandler handler,
                              final Id<Title> id,
                              final Long tmdbId) {
        return new Title(handler, id, tmdbId);
    }

    public static Title load(final Id<Title> id,
                            final Long tmdbId) {
        return new Title(null, id, tmdbId);
    }

    private void selfValidate(final NotificationHandler handler) {
        if (handler == null) return;
    }
}
