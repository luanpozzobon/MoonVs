package luan.moonvs.repositories;

import luan.moonvs.models.entities.MovieList;
import luan.moonvs.models.entities.MovieListId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieListRepository extends JpaRepository<MovieList, MovieListId> {
    Optional<MovieList> findTopByOrderByIdListDesc();
    Optional<MovieList> findByIdList(Long idList);
    Optional<List<MovieList>> findByIdUser(UUID idUser);
    boolean existsByIdList(Long idList);
}
