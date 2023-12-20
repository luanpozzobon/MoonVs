package luan.moonvs.models.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProfileRequest(String biography,
                             boolean isPrivate,
                             int favoriteMovie, // TODO - Change to 'Content'
                             int favoriteSeries) // TODO - Change to 'Content'
{ }