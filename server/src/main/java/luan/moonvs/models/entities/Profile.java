package luan.moonvs.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import luan.moonvs.models.enums.Privacy;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
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
    @Enumerated(EnumType.STRING)
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
}
