package luan.moonvs.repositories;

import jakarta.transaction.Transactional;
import luan.moonvs.models.entities.MovieListContent;
import luan.moonvs.models.entities.MovieListContentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieListContentRepository extends JpaRepository<MovieListContent, MovieListContentId> {
    List<MovieListContent> findAllByIdList(Long idList);

    @Transactional
    @Modifying
    @Query("""
            DELETE FROM lists_content c
            WHERE c.idList = :id_list
            """)
    void deleteByIdList(@Param("id_list") Long idList);
}
