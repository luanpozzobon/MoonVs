package luan.moonvs.models.tmdb_responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Deprecated
@JsonIgnoreProperties(ignoreUnknown = true)
public record TmdbTv(int id,
                     List<TmdbGenres> genres,
                     boolean adult,
                     @JsonProperty("original_name")
                     String originalTitle,
                     @JsonProperty("name")
                     String ptTitle,
                     String overview,
                     @JsonProperty("poster_path")
                     String posterPath,
                     @JsonProperty("vote_average")
                     double voteAvg,
                     @JsonProperty("vote_count")
                     int voteCount) { }