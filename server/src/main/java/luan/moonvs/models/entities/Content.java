package luan.moonvs.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import luan.moonvs.models.enums.ContentType;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Data

@Entity(name = "content")
@Table(name = "content_catalog")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_content",
            nullable = false,
            unique = true)
    private int idContent;

    @Column(name = "id_tmdb",
            nullable = false,
            unique = true)
    private int idTmdb;

    @Column(name = "original_title",
            nullable = false)
    private String originalTitle;

    @Column(name = "pt_title",
            nullable = false)
    private String ptTitle;

    @Column(name = "overview")
    private String overview;

    @Column(name = "is_adult")
    private boolean isAdult;

    @Type(ListArrayType.class)
    @Column(name = "genres",
            columnDefinition = "character_varying[]")
    private List<String> genres;

    @Column(name = "poster_path")
    private String posterPath;

    @Column(name = "tmdb_vote_avg")
    private double tmdbVoteAvg;

    @Column(name = "tmdb_vote_count")
    private int tmdbVoteCount;

    @Type(JsonType.class)
    @Column(name = "watch_provider",
            columnDefinition = "jsonb")
    private Map<String, List<String>> watchProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false)
    private ContentType contentType;
}