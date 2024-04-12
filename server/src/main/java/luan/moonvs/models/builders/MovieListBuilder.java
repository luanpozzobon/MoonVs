package luan.moonvs.models.builders;

import luan.moonvs.models.entities.MovieList;
import luan.moonvs.models.entities.MovieListId;
import luan.moonvs.models.requests.MovieListRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MovieListBuilder {

    private MovieList movieList;

    public MovieListBuilder() {
        this.movieList = new MovieList();
    }

    public static MovieListBuilder create(MovieListRequest movieListRequest, Long idList) throws IllegalArgumentException {
        MovieListBuilder builder = create();
        builder.addMovieListId(idList, movieListRequest.idUser());
        builder.addListName(movieListRequest.listName());
        return builder;
    }

    public static MovieListBuilder create(MovieList movieList) {
        MovieListBuilder builder = create();
        builder.movieList = new MovieList(movieList);
        return builder;
    }

    public static MovieListBuilder create() {
        return new MovieListBuilder();
    }

    public MovieListBuilder addMovieListId(Long idList, UUID idUser) throws IllegalArgumentException {
        final String ID_NULL = "The User id should be provided!";

        if (idUser == null)
            throw new IllegalArgumentException(ID_NULL);

        return addMovieListId(new MovieListId(idList, idUser));
    }

    public MovieListBuilder addMovieListId(MovieListId movieListId) {
        this.movieList.setMovieListId(movieListId);
        return this;
    }

    public MovieListBuilder addListName(String listName) throws IllegalArgumentException {
        final String ILLEGAL_NAME = "The list should contain a name, between 1 and 64 characters!";

        if (listName.isEmpty() || listName.length() > 64)
            throw new IllegalArgumentException(ILLEGAL_NAME);

        this.movieList.setListName(listName);
        return this;
    }

    public MovieListBuilder addListDescription(String listDescription) throws IllegalArgumentException {
        final String ILLEGAL_DESCRIPTION = "The description can't be over 255 characters!";

        if (listDescription.length() > 255)
            throw new IllegalArgumentException(ILLEGAL_DESCRIPTION);

        this.movieList.setListDescription(listDescription);
        return this;
    }

    public MovieList build() {
        return this.movieList;
    }
}
