package luan.moonvs.models.tmdb_responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Deprecated
@JsonIgnoreProperties(ignoreUnknown = true)
public record TmdbMovie(int id,
                        List<TmdbGenres> genres,
                        boolean adult,
                        @JsonProperty("original_title")
                        String originalTitle,
                        @JsonProperty("title")
                        String ptTitle,
                        String overview,
                        @JsonProperty("vote_average")
                        double voteAvg,
                        @JsonProperty("poster_path")
                        String posterPath,
                        @JsonProperty("vote_count")
                        int voteCount) { }
