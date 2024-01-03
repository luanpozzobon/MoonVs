package luan.moonvs.services;

import jakarta.persistence.EntityNotFoundException;
import luan.moonvs.models.builders.RatingBuilder;
import luan.moonvs.models.entities.ContentAndUserId;
import luan.moonvs.models.entities.Rating;
import luan.moonvs.models.entities.User;
import luan.moonvs.models.requests.RateRequest;
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

    @Deprecated
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

    public ResponseEntity<Rating> addOrEditRating(int idContent, RateRequest rateRequest) {
        User authUser = accountService.getAuthenticatedUser();
        Rating rating;
        try {
            rating = repository.getReferenceById(new ContentAndUserId(authUser.getIdUser(), idContent));

            rating = RatingBuilder.create(rating)
                    .addRating(rateRequest.ratingValue())
                    .addCommentary(rateRequest.commentary())
                    .build();
        } catch (EntityNotFoundException e) {
            rating = RatingBuilder.create()
                    .withId(authUser.getIdUser(), idContent)
                    .addRating(rateRequest.ratingValue())
                    .addCommentary(rateRequest.commentary())
                    .build();
        }

        repository.save(rating);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(rating);
    }

    public ResponseEntity<Rating> getUserRating(int idContent) {
        User authUser = accountService.getAuthenticatedUser();
        ContentAndUserId idRating = new ContentAndUserId(authUser.getIdUser(), idContent);

        Rating rating = repository.getReferenceById(idRating);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(rating);
    }

    public ResponseEntity<Float> getAvgRating(int idContent) {
        Float avgRating = repository.getAverageRatingByContent(idContent);
        if (avgRating == null)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(avgRating);
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
