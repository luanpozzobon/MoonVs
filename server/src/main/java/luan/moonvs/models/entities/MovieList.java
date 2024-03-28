package luan.moonvs.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
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

    /*
    @ManyToMany
    @JoinColumn(name = "id_content")
    private List<MovieListContent> contents;
    */

    public MovieList(MovieList movieList) {
        this.idList = movieList.getIdList();
        this.idUser = movieList.getIdUser();
        this.listName = movieList.getListName();
        this.listDescription = movieList.getListDescription();
        this.createdAt = movieList.getCreatedAt();
    }

    public void setMovieListId(MovieListId movieListId) {
        this.setIdList(movieListId.getIdList());
        this.setIdUser(movieListId.getIdUser());
    }
}
