package luan.moonvs.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity(name = "ratings")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Rating {
    @EmbeddedId
    private ContentAndUserId idRating;

    @ManyToOne
    @JoinColumn(name = "id_content",
                insertable = false,
                updatable = false,
                referencedColumnName = "id_content")
    private Content content;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_user",
                insertable = false,
                updatable = false,
                referencedColumnName = "id_user")
    private Profile profile;

    @Column(name = "rating_value",
            nullable = false)
    private float ratingValue;

    @Column(name = "commentary")
    private String commentary;

    @CreationTimestamp
    @Column(name = "added_at",
            nullable = false)
    private LocalDateTime addedAt;
}
