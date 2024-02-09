package luan.moonvs.repositories;

import luan.moonvs.models.entities.MovieList;
import luan.moonvs.models.entities.MovieListId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieListRepository extends JpaRepository<MovieList, MovieListId> { }
