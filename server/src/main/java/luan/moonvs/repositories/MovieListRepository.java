package luan.moonvs.repositories;

import luan.moonvs.models.entities.MovieList;
import luan.moonvs.models.entities.MovieListId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieListRepository extends JpaRepository<MovieList, MovieListId> {
    Optional<MovieList> findTopByOrderByIdListDesc();
}
