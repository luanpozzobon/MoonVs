package luan.moonvs.models.responses;

import lombok.AllArgsConstructor;
import luan.moonvs.models.entities.MovieList;

public record MovieListResponse(long idList,
                                String listName,
                                String listDescription) {

    public MovieListResponse(MovieList m) {
        this(m.getIdList(), m.getListName(), m.getListDescription());
    }
}
