package luan.moonvs.repositories;

import luan.moonvs.models.entities.ContentAndUserId;
import luan.moonvs.models.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, ContentAndUserId> {

    List<Rating> getAllRatingsByIdRatingIdUser(UUID idUser);

    @Query("""
            SELECT AVG(r.ratingValue)
                FROM ratings r
                WHERE r.idRating.idContent = :id_content
           """)
    Float getAverageRatingByContent(@Param("id_content") int idContent);
}
