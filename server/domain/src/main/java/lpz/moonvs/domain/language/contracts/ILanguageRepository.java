package lpz.moonvs.domain.language.contracts;

import lpz.moonvs.domain.language.entity.Language;
import lpz.moonvs.domain.seedwork.valueobject.Id;

import java.util.List;

public interface ILanguageRepository {
    List<Language> findAll();

    Language findById(final Id<Language> id);

    Language findByCountryCode(final String countryCode);

    Language findByLanguageCode(final String languageCode);

    Language findByName(final String name);
}
