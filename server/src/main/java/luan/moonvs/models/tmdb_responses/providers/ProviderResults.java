package luan.moonvs.models.tmdb_responses.providers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProviderResults(Map<String, ProviderType> results) {}
