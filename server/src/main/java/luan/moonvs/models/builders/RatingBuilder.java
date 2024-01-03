package luan.moonvs.models.builders;

import luan.moonvs.models.entities.ContentAndUserId;
import luan.moonvs.models.entities.Rating;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RatingBuilder {
    private Rating rating;

    public static RatingBuilder create() {
        RatingBuilder ratingBuilder = new RatingBuilder();
        ratingBuilder.rating = new Rating();
        ratingBuilder.rating.setIdRating(new ContentAndUserId());
        return ratingBuilder;
    }

    public static RatingBuilder create(Rating rating) {
        RatingBuilder ratingBuilder = new RatingBuilder();
        ratingBuilder.rating = rating;
        return ratingBuilder;
    }

    public RatingBuilder withId(UUID idUser, int idContent) {
        ContentAndUserId idRating = new ContentAndUserId(idUser, idContent);
        this.rating.setIdRating(idRating);

        return this;
    }

    public RatingBuilder addRating(float ratingValue) throws IllegalArgumentException {
        if (ratingValue < 0.0 || ratingValue > 10.0)
            throw new IllegalArgumentException("A avaliação deve estar entre 0.0 e 10.0");

        this.rating.setRatingValue(ratingValue);
        return this;
    }

    public RatingBuilder addCommentary(String commentary) {
        if (commentary != null && !commentary.isBlank())
            this.rating.setCommentary(commentary);

        return this;
    }

    public Rating build() {
        return rating;
    }


}
