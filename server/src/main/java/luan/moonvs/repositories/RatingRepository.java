package luan.moonvs.repositories;

import luan.moonvs.models.entities.ContentAndUserId;
import luan.moonvs.models.entities.Profile;
import luan.moonvs.models.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, ContentAndUserId> {
    List<Rating> getByIdRatingIdUser(UUID idUser);
}
