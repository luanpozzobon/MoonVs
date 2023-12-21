package luan.moonvs.services;

import jakarta.persistence.EntityNotFoundException;
import luan.moonvs.models.builders.RatingBuilder;
import luan.moonvs.models.entities.ContentAndUserId;
import luan.moonvs.models.entities.Rating;
import luan.moonvs.models.entities.User;
import luan.moonvs.models.requests.RatingRequest;
import luan.moonvs.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    private RatingRepository repository;
    private AccountService accountService;

    @Autowired
    public RatingService(RatingRepository repository, AccountService accountService) {
        this.repository = repository;
        this.accountService = accountService;
    }

    public ResponseEntity<Rating> newRating(RatingRequest ratingRequest) {
        User authUser = accountService.getAuthenticatedUser();
        Rating rating = null;

        rating = RatingBuilder.create()
                .withId(authUser.getIdUser(), ratingRequest.idContent())
                .addRating(ratingRequest.ratingValue())
                .addCommentary(ratingRequest.commentary())
                .build();

        repository.save(rating);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(rating);
    }

    public ResponseEntity<Rating> getRating(int idContent) {
        User authUser = accountService.getAuthenticatedUser();
        ContentAndUserId idRating = new ContentAndUserId(authUser.getIdUser(), idContent);

        Rating rating = repository.getReferenceById(idRating);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(rating);
    }

    public ResponseEntity<?> getAllUserRatings() {
        User authUser = accountService.getAuthenticatedUser();
        try {
            List<Rating> ratingList = repository.getByIdRatingIdUser(authUser.getIdUser());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ratingList);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Perfil não encontrado");
        }
    }
}
