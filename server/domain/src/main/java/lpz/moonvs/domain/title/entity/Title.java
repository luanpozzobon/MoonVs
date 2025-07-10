package lpz.moonvs.domain.title.entity;

import lpz.moonvs.domain.seedwork.exception.DomainValidationException;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.dto.TitleDB;
import lpz.moonvs.domain.title.dto.tmdb.TitleDetails;
import lpz.moonvs.domain.title.dto.tmdb.TitleSearch;
import lpz.moonvs.domain.title.entity.schema.TitleSchema;
import lpz.moonvs.domain.title.validation.TitleValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Title {
    private final Id<Title> id;
    private final Integer tmdbId;
    private final String title;
    private final Type type;
    private final List<TitleTranslation> translations;
    private final List<Genre> genres;
    private final LocalDate releaseDate;
    private final Integer screenTime;
    private final Boolean adult;

    public Id<Title> getId() {
        return id;
    }

    public Integer getTmdbId() {
        return tmdbId;
    }

    public String getTitle() {
        return title;
    }

    public Type getType() {
        return type;
    }

    public List<TitleTranslation> getTranslations() {
        return translations;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public Integer getScreenTime() {
        return screenTime;
    }

    public Boolean isAdult() {
        return adult;
    }

    private Title(final Builder builder) {
        this.tmdbId = builder.tmdbId;
        this.title = builder.title;
        this.type = builder.type;

        this.id = builder.id;
        this.translations = builder.translations;
        this.genres = builder.genres;
        this.releaseDate = builder.releaseDate;
        this.screenTime = builder.screenTime;
        this.adult = builder.adult;
    }

    public static Title fromSearch(final NotificationHandler handler,
                                   final TitleSearch titleSearch) {
        return new Builder(titleSearch.id(), titleSearch.title(), Type.valueOf(titleSearch.mediaType()))
                .translations(List.of(TitleTranslation.fromSearch(titleSearch.overview(), titleSearch.posterPath())))
                .build()
                .selfValidate(handler);
    }

    public static Title fromDetails(final NotificationHandler handler,
                                    final TitleDetails titleDetails) {
        Builder builder = new Builder(titleDetails.id(), titleDetails.originalTitle(), Type.valueOf(titleDetails.mediaType()));

        List<Genre> genres = titleDetails.genres().stream().map(genre ->
            Genre.fromDetails(titleDetails.languageId(), genre)
        ).toList();

        List<TitleTranslation> translations = List.of(TitleTranslation.fromDetails(
                Id.from(titleDetails.languageId()), titleDetails.translation()));

        builder.genres(genres)
                .translations(translations)
                .releaseDate(titleDetails.releaseDate())
                .screenTime(titleDetails.screenTime());

        if (titleDetails.adult())
            builder.isAdult();

        return builder.build()
                .selfValidate(handler);
    }

    public static Title load(TitleDB titleDB) {
        Builder builder = new Builder(titleDB.tmdbId(), titleDB.title(), titleDB.type())
                .id(titleDB.id())
                .translations(titleDB.translations())
                .genres(titleDB.genres())
                .releaseDate(titleDB.releaseDate())
                .screenTime(titleDB.screenTime());

        if (titleDB.adult())
            builder.isAdult();

        return builder.build();
    }

    private Title selfValidate(final NotificationHandler handler) {
        new TitleValidator(handler).validate(this);

        if (handler.hasError())
            throw new DomainValidationException(handler.getErrors());

        return this;
    }

    public static class Builder {
        private final Integer tmdbId;
        private final String title;
        private final Type type;
        private Id<Title> id;
        private List<TitleTranslation> translations;
        private List<Genre> genres;
        private LocalDate releaseDate;
        private Integer screenTime;
        private Boolean adult;

        public Builder(final Integer tmdbId, final String title, final Type type) {
            final String message = "'%s' cannot be null.";
            this.tmdbId = Objects.requireNonNull(tmdbId, String.format(message, TitleSchema.TMDB_ID));
            this.title = Objects.requireNonNull(title, String.format(message, TitleSchema.TITLE));
            this.type = Objects.requireNonNull(type, String.format(message, TitleSchema.TYPE));

            this.adult = false;
        }

        public Builder id(final Id<Title> id) {
            this.id = id;
            return this;
        }

        public Builder translations(final List<TitleTranslation> translations) {
            this.translations = translations;
            return this;
        }

        public Builder genres(final List<Genre> genres) {
            this.genres = genres;
            return this;
        }

        public Builder releaseDate(final LocalDate releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public Builder screenTime(final Integer screenTime) {
            this.screenTime = screenTime;
            return this;
        }

        public Builder isAdult() {
            this.adult = true;
            return this;
        }

        public Title build() {
            return new Title(this);
        }
    }
}
