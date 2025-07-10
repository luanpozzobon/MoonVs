package lpz.moonvs.domain.title.entity;

import lpz.moonvs.domain.language.entity.Language;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.dto.tmdb.TitleDetails;
import lpz.moonvs.domain.title.dto.tmdb.Translations;

public class TitleTranslation {
    private final Id<Title> titleId;
    private final Id<Language> languageId;
    private final String title;
    private final String tagline;
    private final String overview;
    private String poster;

    private TitleTranslation(final Id<Title> titleId,
                             final Id<Language> languageId,
                             final String title,
                             final String tagline,
                             final String overview,
                             final String poster) {
        this.titleId = titleId;
        this.languageId = languageId;
        this.title = title;
        this.tagline = tagline;
        this.overview = overview;
        this.poster = poster;
    }

    public static TitleTranslation fromSearch(final String overview,
                                              final String poster) {
        return new TitleTranslation(null, null, null, null, overview, poster);
    }

    public static TitleTranslation fromDetails(final Id<Language> languageId,
                                               final TitleDetails.Translation translation) {
        return new TitleTranslation(null, languageId, translation.title(), translation.tagline(), translation.overview(), translation.posterPath());
    }

    public static TitleTranslation fromTranslation(final Id<Title> titleId,
                                                   final Id<Language> languageId,
                                                   final Translations.Translation translation) {
        Translations.Data data = translation.data();
        return new TitleTranslation(titleId, languageId, data.title(), data.tagline(), data.overview(), null);
    }

    public static TitleTranslation load(final Id<Title> titleId,
                                        final Id<Language> languageId,
                                        final String title,
                                        final String tagline,
                                        final String overview,
                                        final String poster) {
        return new TitleTranslation(titleId, languageId, title, tagline, overview, poster);
    }

    public Id<Title> getTitleId() {
        return titleId;
    }

    public Id<Language> getLanguageId() {
        return languageId;
    }

    public String getTitle() {
        return title;
    }

    public String getTagline() {
        return tagline;
    }

    public String getOverview() {
        return overview;
    }

    public void addPoster(final String poster) {
        this.poster = poster;
    }

    public String getPoster() {
        return poster;
    }
}
