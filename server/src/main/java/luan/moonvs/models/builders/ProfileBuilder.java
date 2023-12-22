package luan.moonvs.models.builders;

import luan.moonvs.models.entities.Content;
import luan.moonvs.models.entities.Profile;
import luan.moonvs.models.entities.User;
import luan.moonvs.models.enums.Privacy;
import luan.moonvs.models.requests.ProfileRequest;
import org.springframework.stereotype.Component;

@Component
public class ProfileBuilder {
    private Profile profile;

    public ProfileBuilder() {
        profile = new Profile();
    }

    public ProfileBuilder fromAuthUser(User authUser) {
        this.profile.setUser(authUser);
        return this;
    }

    public ProfileBuilder createProfile(ProfileRequest profileRequest) {
        this.profile = new Profile();
        return this
                .withBio(profileRequest.biography())
                .privacy(profileRequest.isPrivate());
        // TODO - Alterar tipo de favMovie e favSeries
                // .withFavoriteMovie(profileRequest.favoriteMovie())
                // .withFavoriteSeries(profileRequest.favoriteSeries());
    }

    public ProfileBuilder editProfile(Profile profile, ProfileRequest profileRequest) {
        this.profile = profile;
        return this
                .withBio(profileRequest.biography())
                .privacy(profileRequest.isPrivate());
        // TODO - Alterar tipo de favMovie e favSeries
            // .withFavoriteMovie(profileRequest.favoriteMovie())
            // .withFavoriteSeries(profileRequest.favoriteSeries());
    }

    public ProfileBuilder withBio(String biography) {
        if (biography == null)
            this.profile.setBiography("");

        if (biography.length() > 255)
            throw new IllegalArgumentException("O campo 'bio' deve conter no máximo 255 caracteres!");

        this.profile.setBiography(biography);
        return this;
    }

    public ProfileBuilder privacy(boolean privacy) {
        if (privacy)
            privateProfile();
        else
            publicProfile();

        return this;
    }

    private void publicProfile() {
        this.profile.setPrivacy(Privacy.PUBLIC);
    }

    private void privateProfile() {
        this.profile.setPrivacy(Privacy.PRIVATE);
    }

    public ProfileBuilder withFavoriteMovie(Content favoriteMovie) {
        if (favoriteMovie != null && favoriteMovie.getIdContent() > 0)
            this.profile.setFavoriteMovie(favoriteMovie);

        return this;
    }

    public ProfileBuilder withFavoriteSeries(Content favoriteSeries) {
        if (favoriteSeries != null && favoriteSeries.getIdContent() > 0)
            this.profile.setFavoriteSeries(favoriteSeries);

        return this;
    }

    public Profile build() {
        return profile;
    }
}
