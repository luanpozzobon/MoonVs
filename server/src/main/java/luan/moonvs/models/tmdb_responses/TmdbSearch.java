package luan.moonvs.models.tmdb_responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import luan.moonvs.utils.deserializers.TmdbSearchDeserializer;
import luan.moonvs.models.enums.ContentType;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = TmdbSearchDeserializer.class)
public record TmdbSearch(int id,
                         @JsonProperty("original_title")
                         String originalTitle,
                         String overview,
                         @JsonProperty("media_type")
                         ContentType contentType,
                         @JsonProperty("vote_average")
                         double voteAvg) { }


