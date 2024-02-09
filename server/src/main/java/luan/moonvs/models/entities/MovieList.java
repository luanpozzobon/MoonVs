package luan.moonvs.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor

@Entity
@Table(name = "movie_lists")
public class MovieList {
    @EmbeddedId
    private MovieListId movieListId;

    @Column(name = "list_name", nullable = false, length = 64)
    private String listName;

    @Column(name = "list_description")
    private String listDescription;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
