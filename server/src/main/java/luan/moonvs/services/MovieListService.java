package luan.moonvs.services;

import luan.moonvs.models.builders.MovieListBuilder;
import luan.moonvs.models.entities.MovieList;
import luan.moonvs.models.requests.MovieListRequest;
import luan.moonvs.models.responses.Response;
import luan.moonvs.repositories.MovieListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovieListService {

    @Autowired
    private MovieListRepository repository;

    public synchronized Response<MovieList> create(MovieListRequest movieListRequest) {
        Long idList = movieListRequest.idList();
        if (idList == null)
            idList = getNextIdList();

        try {
            MovieList movieList =
                    MovieListBuilder.create(movieListRequest, idList)
                    .addListDescription(movieListRequest.listDescription())
                    .build();

            repository.save(movieList);
            return new Response<>(HttpStatus.CREATED, movieList);
        } catch (IllegalArgumentException e) {
            return new Response<>(HttpStatus.BAD_REQUEST, new MovieList(), e.getMessage());
        }
    }

    private Long getNextIdList() {
        Optional<MovieList> lastList = repository.findTopByOrderByIdListDesc();
        return lastList.map(movieList -> movieList.getIdList() + 1).orElse(1L);

    }
}
