package luan.moonvs.models.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RatingRequest(int idContent,
                            float ratingValue,
                            String commentary) { }
