package luan.moonvs.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor

@Entity(name = "lists_content")
@Table(name = "lists_content")
@IdClass(MovieListContentId.class)
public class MovieListContent {
    @Id
    @Column(name = "id_list",
            nullable = false)
    private Long idList;

    @Id
    @Column(name = "id_content",
            nullable = false)
    private Integer idContent;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_content")
    private Content content;

    @CreationTimestamp
    @Column(name = "date_added",
            nullable = false)
    private LocalDateTime dateAdded;

    public MovieListContent(Long idList, Integer idContent) {
        this.idList = idList;
        this.idContent = idContent;
    }
}
