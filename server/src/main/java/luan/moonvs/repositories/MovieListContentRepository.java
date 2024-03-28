package luan.moonvs.repositories;

import luan.moonvs.models.entities.MovieListContent;
import luan.moonvs.models.entities.MovieListContentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieListContentRepository extends JpaRepository<MovieListContent, MovieListContentId> {
    List<MovieListContent> findAllByIdList(Long idList);
}
