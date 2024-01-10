package luan.moonvs.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import luan.moonvs.models.enums.Privacy;
import luan.moonvs.utils.converters.PrivacyConverter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity(name = "profiles")
public class Profile {
    @Id
    @Column(name = "id_user")
    private UUID idUser;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_user")
    private User user;

    @Column(name = "biography")
    private String biography;

    @Column(name = "privacy")
    @Convert(converter = PrivacyConverter.class)
    private Privacy privacy;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "fav_movie",
                referencedColumnName = "id_content")
    private Content favoriteMovie;

    @ManyToOne
    @JoinColumn(name = "fav_series",
                referencedColumnName = "id_content")
    private Content favoriteSeries;

    @OneToMany(mappedBy = "profile",
               cascade = CascadeType.REMOVE)
    private List<Rating> ratings;

    public Profile(Profile profile) {
        this.idUser = profile.getIdUser();
        this.user = new User(profile.getUser());
        this.biography = profile.getBiography();
        this.privacy = profile.getPrivacy();
        this.createdAt = profile.getCreatedAt();
        this.favoriteMovie = profile.getFavoriteMovie();
        this.favoriteSeries = profile.getFavoriteSeries();
    }
}
