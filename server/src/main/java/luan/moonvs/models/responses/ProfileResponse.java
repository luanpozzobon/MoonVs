package luan.moonvs.models.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import luan.moonvs.models.entities.Content;
import luan.moonvs.models.entities.Profile;
import luan.moonvs.models.enums.Privacy;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProfileResponse(UUID idUser,
                              String username,
                              String biography,
                              Privacy privacy,
                              LocalDateTime createdAt,
                              Content favoriteMovie,
                              Content favoriteSeries) {

    public ProfileResponse(Profile profile) {
        this(profile.getIdUser(),
             profile.getUser().getUsername(),
             profile.getBiography(),
             profile.getPrivacy(),
             profile.getCreatedAt(),
             profile.getFavoriteMovie(),
             profile.getFavoriteSeries());
    }

}
