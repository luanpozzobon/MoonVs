package luan.moonvs.repositories;

import luan.moonvs.models.entities.MovieListContent;
import luan.moonvs.models.entities.MovieListContentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieListContentRepository extends JpaRepository<MovieListContent, MovieListContentId> { }
