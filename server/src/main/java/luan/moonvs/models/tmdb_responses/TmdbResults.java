package luan.moonvs.models.tmdb_responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TmdbResults(int page,
                          List<TmdbSearch> results,
                          @JsonProperty("total_pages")
                          int totalPages) { }
