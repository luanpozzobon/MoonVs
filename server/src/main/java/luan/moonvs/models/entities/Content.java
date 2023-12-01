package luan.moonvs.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import luan.moonvs.models.enums.ContentType;

import java.util.List;

@NoArgsConstructor
@Data

@Entity(name = "content")
@Table(name = "content_catalog")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_content", nullable = false, unique = true)
    private int idContent;
    @Column(name = "id_tmdb", nullable = false, unique = true)
    private int idTmdb;
    @Column(name = "original_title", nullable = false)
    private String originalTitle;
    @Column(name = "pt_title", nullable = false)
    private String ptTitle;
    @Column(name = "overview")
    private String overview;
    @Column(name = "is_adult")
    private boolean isAdult;
    @Column(name = "genres")
    private List<String> genres;
    @Column(name = "tmdb_vote_avg")
    private double tmdbVoteAvg;
    @Column(name = "tmdb_vote_count")
    private int tmdbVoteCount;
    @Column(name = "watch_provider")
    private List<String> watchProvider;
    @Column(name = "content_type", nullable = false)
    private ContentType contentType;
}