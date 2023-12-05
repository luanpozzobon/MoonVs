package luan.moonvs.models.tmdb_responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TmdbTv(int id,
                     List<TmdbGenres> genres,
                     boolean adult,
                     @JsonProperty("original_name")
                     String originalTitle,
                     String overview,
                     @JsonProperty("vote_average")
                     double voteAvg,
                     @JsonProperty("vote_count")
                     int voteCount) { }