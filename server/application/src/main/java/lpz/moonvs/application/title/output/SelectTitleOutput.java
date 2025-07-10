package lpz.moonvs.application.title.output;

import lpz.moonvs.domain.title.entity.*;

import java.time.LocalDate;
import java.util.List;

public record SelectTitleOutput(Integer id,
                                Integer tmdbId,
                                String title,
                                Type type,
                                List<Translation> translations,
                                List<Genre> genres,
                                LocalDate releaseDate,
                                Integer screenTime,
                                Boolean adult
) {
    public static SelectTitleOutput from(final Title title) {
        return new SelectTitleOutput(
                Integer.parseInt(title.getId().getValue()),
                title.getTmdbId(),
                title.getTitle(),
                title.getType(),
                title.getTranslations().stream().map(Translation::from).toList(),
                title.getGenres().stream().map(Genre::from).toList(),
                title.getReleaseDate(),
                title.getScreenTime(),
                title.isAdult()
        );
    }


    public record Translation(Integer languageId,
                              String title,
                              String tagline,
                              String overview,
                              String poster) {
        public static Translation from(final TitleTranslation translation) {
            return new Translation(
                    Integer.parseInt(translation.getLanguageId().getValue()),
                    translation.getTitle(),
                    translation.getTagline(),
                    translation.getOverview(),
                    translation.getPoster()
            );
        }
    }

    public record Genre(Integer id,
                        Integer tmdbId,
                        List<Translation> translations) {
        public static Genre from(final lpz.moonvs.domain.title.entity.Genre genre) {
            return new Genre(
                    Integer.parseInt(genre.getId().getValue()),
                    genre.getTmdbId(),
                    genre.getTranslations().stream().map(Translation::from).toList()
            );
        }

        public record Translation(Integer languageId,
                                  String name) {
            public static Translation from(final lpz.moonvs.domain.title.entity.GenreTranslation translation) {
                return new Translation(
                        Integer.parseInt(translation.getLanguageId().getValue()),
                        translation.getName()
                );
            }
        }
    }
}
