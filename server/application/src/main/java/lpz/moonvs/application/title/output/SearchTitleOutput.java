package lpz.moonvs.application.title.output;

import lpz.moonvs.application.title.SearchProvider;
import lpz.moonvs.domain.title.entity.Type;

import java.util.List;

public record SearchTitleOutput(List<Data> titles,
                                Metadata metadata) {

    public record Data(Integer id,
                       String title,
                       String poster,
                       String overview,
                       Type type) {
    }

    public record Metadata(SearchProvider provider,
                           Integer page,
                           Integer totalPages) { }
}
