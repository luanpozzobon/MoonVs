package luan.moonvs.models.tmdb_responses.providers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Provider(@JsonProperty("provider_name") String providerName) { }
