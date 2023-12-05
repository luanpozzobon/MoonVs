package luan.moonvs.models.tmdb_responses.providers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProviderType(List<Provider> flatrate, List<Provider> buy, List<Provider> rent) {
}
