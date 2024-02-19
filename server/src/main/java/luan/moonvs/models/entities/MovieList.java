package luan.moonvs.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor

@Entity
@Table(name = "movie_lists")
@IdClass(MovieListId.class)
public class MovieList {
    @Id
    @Column(name = "id_list")
    private long idList;

    @Id
    @Column(name = "id_user")
    private UUID idUser;

    @Column(name = "list_name", nullable = false, length = 64)
    private String listName;

    @Column(name = "list_description")
    private String listDescription;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public void setMovieListId(MovieListId movieListId) {
        this.setIdList(movieListId.getIdList());
        this.setIdUser(movieListId.getIdUser());
    }
}
