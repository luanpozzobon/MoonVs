package lpz.moonvs.domain.title.dto;

import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Genre;
import lpz.moonvs.domain.title.entity.Title;
import lpz.moonvs.domain.title.entity.TitleTranslation;
import lpz.moonvs.domain.title.entity.Type;

import java.time.LocalDate;
import java.util.List;

public record TitleDB(Id<Title> id,
                      Integer tmdbId,
                      String title,
                      Type type,
                      List<TitleTranslation> translations,
                      List<Genre> genres,
                      String poster,
                      LocalDate releaseDate,
                      Integer screenTime,
                      Boolean adult) { }
