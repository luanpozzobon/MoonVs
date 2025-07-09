package lpz.moonvs.domain.language.entity;

import lpz.moonvs.domain.seedwork.valueobject.Id;

public class Language {
    private final Id<Language> id;
    private final String countryCode;
    private final String languageCode;

    private Language(final Id<Language> id,
                     final String countryCode,
                     final String languageCode) {
        this.id = id;
        this.countryCode = countryCode;
        this.languageCode = languageCode;
    }

    public Id<Language> getId() {
        return id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public static Language create(final String countryCode,
                                  final String languageCode) {
        return new Language(Id.unique(), countryCode, languageCode);
    }

    public static Language load(final Id<Language> id,
                                final String countryCode,
                                final String languageCode) {
        return new Language(id, countryCode, languageCode);
    }
}
