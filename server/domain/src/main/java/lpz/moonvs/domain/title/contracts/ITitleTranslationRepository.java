package lpz.moonvs.domain.title.contracts;

import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Title;
import lpz.moonvs.domain.title.entity.TitleTranslation;

import java.util.List;

public interface ITitleTranslationRepository {
    TitleTranslation save(final TitleTranslation titleTranslation);

    List<TitleTranslation> findAllByTitleId(final Id<Title> titleId);

    TitleTranslation findByIdAndLanguage(final Id<Title> titleId,
                                          final String language);
}
