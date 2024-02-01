package luan.moonvs.models.tmdb_responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import luan.moonvs.utils.deserializers.TmdbContentDeserializer;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = TmdbContentDeserializer.class)
public record TmdbContent(int id,
                          List<String> genres,
                          boolean adult,
                          @JsonProperty("original_title")
                          String originalTitle,
                          @JsonProperty("title")
                          String ptTitle,
                          String overview,
                          @JsonProperty("poster_path")
                          String posterPath,
                          @JsonProperty("vote_average")
                          double voteAvg,
                          @JsonProperty("vote_count")
                          int voteCount) { }
