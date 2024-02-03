package luan.moonvs.services;

import jakarta.persistence.EntityNotFoundException;
import luan.moonvs.models.builders.RatingBuilder;
import luan.moonvs.models.entities.ContentAndUserId;
import luan.moonvs.models.entities.Profile;
import luan.moonvs.models.entities.Rating;
import luan.moonvs.models.entities.User;
import luan.moonvs.models.requests.RateRequest;
import luan.moonvs.models.responses.Response;
import luan.moonvs.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class RatingService {
    private final RatingRepository repository;
    private final AccountService accountService;
    private final ProfileService profileService;

    @Autowired
    public RatingService(RatingRepository repository, AccountService accountService, ProfileService profileService) {
        this.repository = repository;
        this.accountService = accountService;
        this.profileService = profileService;
    }

    public Response<Rating> addOrEditRating(int idContent, RateRequest rateRequest) {
        User user = accountService.getAccount();
        ContentAndUserId idRating = new ContentAndUserId(user.getIdUser(), idContent);
        Rating rating;
        try {
            rating = repository.findById(idRating).orElseThrow();

            rating = RatingBuilder.create(rating)
                    .addRating(rateRequest.ratingValue())
                    .addCommentary(rateRequest.commentary())
                    .build();
        } catch (NoSuchElementException e) {
            rating = RatingBuilder.create(idRating)
                    .addRating(rateRequest.ratingValue())
                    .addCommentary(rateRequest.commentary())
                    .build();
        }

        repository.save(rating);

        return new Response<>(HttpStatus.OK, rating);
    }

    public Response<Rating> getUserRating(int idContent) {
        final String NOT_FOUND = "It wasn't possible to find the rating";
        User user = accountService.getAccount();
        ContentAndUserId idRating = new ContentAndUserId(user.getIdUser(), idContent);

        try {
            Rating rating = repository.getReferenceById(idRating);

            return new Response<>(HttpStatus.OK, rating);
        } catch (EntityNotFoundException e) {
            return new Response<>(HttpStatus.NOT_FOUND, new Rating(), NOT_FOUND);
        }
    }

    public Response<Float> getAvgRating(int idContent) {
        final String NOT_FOUND = "The content has no ratings";
        Float avgRating = repository.getAverageRatingByContent(idContent);
        if (avgRating == null)
            return new Response<>(HttpStatus.NOT_FOUND, Float.NaN, NOT_FOUND);

        return new Response<>(HttpStatus.OK, avgRating);
    }

    public Response<List<Rating>> getUserRatingList(UUID idUser) {
        final String PROFILE_NOT_FOUND = "It wasn't possible to find the profile!",
                     RATINGS_NOT_FOUND = "It wasn't possible to finde this profile's rating!";

        Response<Profile> profile = profileService.getProfile(idUser);
        if (!profile.status().is2xxSuccessful())
            return new Response<>(HttpStatus.NOT_FOUND, Collections.emptyList(), PROFILE_NOT_FOUND);

        try {
            List<Rating> ratingList = repository.getAllRatingsByIdRatingIdUser(idUser);
            return new Response<>(HttpStatus.OK, ratingList);
        } catch (EntityNotFoundException e) {
            return new Response<>(HttpStatus.NOT_FOUND, Collections.emptyList(), RATINGS_NOT_FOUND);
        }
    }
}