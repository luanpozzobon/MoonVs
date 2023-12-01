package luan.moonvs.models.tmdb_responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TmdbResults(List<TmdbSearch> results) { }
