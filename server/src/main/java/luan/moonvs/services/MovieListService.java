package luan.moonvs.services;

import luan.moonvs.models.builders.MovieListBuilder;
import luan.moonvs.models.entities.MovieList;
import luan.moonvs.models.entities.MovieListContent;
import luan.moonvs.models.requests.MovieListRequest;
import luan.moonvs.models.responses.MovieListResponse;
import luan.moonvs.models.responses.Response;
import luan.moonvs.repositories.ContentRepository;
import luan.moonvs.repositories.MovieListContentRepository;
import luan.moonvs.repositories.MovieListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieListService {
    private MovieListRepository listRepository;
    private MovieListContentRepository listContentRepository;
    private ContentRepository contentRepository;


    private final String ID_DOESNT_EXIST = "There's no %s with the given Id!";

    @Autowired
    public MovieListService(MovieListRepository listRepository,
                            MovieListContentRepository listContentRepository,
                            ContentRepository contentRepository) {
        this.listRepository = listRepository;
        this.listContentRepository = listContentRepository;
        this.contentRepository = contentRepository;
    }

    public synchronized Response<MovieList> create(MovieListRequest movieListRequest) {
        Long idList = movieListRequest.idList();
        if (idList == null)
            idList = getNextIdList();

        try {
            MovieList movieList =
                    MovieListBuilder.create(movieListRequest, idList)
                    .addListDescription(movieListRequest.listDescription())
                    .build();

            listRepository.save(movieList);
            return new Response<>(HttpStatus.CREATED, movieList);
        } catch (IllegalArgumentException e) {
            return new Response<>(HttpStatus.BAD_REQUEST, new MovieList(), e.getMessage());
        }
    }

    private Long getNextIdList() {
        Optional<MovieList> lastList = listRepository.findTopByOrderByIdListDesc();
        return lastList.map(movieList -> movieList.getIdList() + 1).orElse(1L);

    }

    public Response<List<MovieListResponse>> getUserLists(UUID idUser) {
        final String NO_LISTS_FOUND = "The user doesn't have any list!";

        Optional<List<MovieList>> movieLists = listRepository.findByIdUser(idUser);
        if (movieLists.isEmpty())
            return new Response<>(HttpStatus.NOT_FOUND, null, NO_LISTS_FOUND);

        var response = new ArrayList<>(movieLists.get()
                .stream()
                .map(MovieListResponse::new)
                .toList());

        return new Response<>(HttpStatus.OK, response);
    }

    public Response<?> delete(Long idList) {
        Optional<MovieList> movieList = listRepository.findByIdList(idList);
        if (movieList.isEmpty())
            return new Response(HttpStatus.BAD_REQUEST, String.format(ID_DOESNT_EXIST, "list"));

        listRepository.delete(movieList.get());
        return new Response(HttpStatus.OK, "");
    }

    public Response<MovieListContent> add(Long idList, Integer idContent) {
        Optional<MovieList> movieList = listRepository.findByIdList(idList);
        if (movieList.isEmpty())
            return new Response<>(HttpStatus.BAD_REQUEST, null, String.format(ID_DOESNT_EXIST, "list"));

        if (!contentRepository.existsById(idContent))
            return new Response<>(HttpStatus.BAD_REQUEST, null, String.format(ID_DOESNT_EXIST, "content"));

        var movieListContent = new MovieListContent(idList, idContent);
        try {
            movieListContent = listContentRepository.save(movieListContent);

            return new Response<>(HttpStatus.CREATED, movieListContent);
        } catch (IllegalArgumentException e) {
            return new Response<>(HttpStatus.BAD_REQUEST, null, e.getMessage());
        }
    }
}
