package lpz.moonvs.domain.title.contracts.model;

import lpz.moonvs.domain.language.entity.Language;
import lpz.moonvs.domain.seedwork.valueobject.Id;

public record TitleSearchQuery(String title,
                               Id<Language> languageId,
                               Integer page) { }
