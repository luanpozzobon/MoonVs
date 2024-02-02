package luan.moonvs.models.builders;

import luan.moonvs.models.entities.ContentAndUserId;
import luan.moonvs.models.entities.Rating;
import org.springframework.stereotype.Component;

@Component
public class RatingBuilder {
    private Rating rating;

    public static RatingBuilder create(ContentAndUserId contentAndUserId) {
        RatingBuilder ratingBuilder = new RatingBuilder();
        ratingBuilder.rating = new Rating();
        ratingBuilder.rating.setIdRating(contentAndUserId);

        return ratingBuilder;
    }

    public static RatingBuilder create(Rating rating) {
        RatingBuilder ratingBuilder = new RatingBuilder();
        ratingBuilder.rating = rating;
        return ratingBuilder;
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