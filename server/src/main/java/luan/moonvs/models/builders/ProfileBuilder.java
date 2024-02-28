package luan.moonvs.models.builders;

import luan.moonvs.models.entities.Content;
import luan.moonvs.models.entities.Profile;
import luan.moonvs.models.entities.User;
import luan.moonvs.models.enums.Privacy;
import org.springframework.stereotype.Component;

@Component
public class ProfileBuilder {
    private Profile profile;

    public ProfileBuilder() {
        profile = new Profile();
    }

    public static ProfileBuilder create() {
        ProfileBuilder profileBuilder = new ProfileBuilder();
        profileBuilder.profile = new Profile();

        return profileBuilder;
    }

    public static ProfileBuilder create(User user) {
        ProfileBuilder profileBuilder = new ProfileBuilder();
        profileBuilder.profile = new Profile();
        profileBuilder.profile.setUser(user);

        return profileBuilder;
    }

    public static ProfileBuilder create(Profile profile) {
        ProfileBuilder profileBuilder = new ProfileBuilder();
        profileBuilder.profile = new Profile(profile);

        return profileBuilder;
    }

    public ProfileBuilder withBio(String biography) {
        final String TOO_LONG = "The 'bio' must contain at most 255 characters!";

        if (biography == null) {
            this.profile.setBiography("");
            return this;
        }

        if (biography.length() > 255)
            throw new IllegalArgumentException(TOO_LONG);

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