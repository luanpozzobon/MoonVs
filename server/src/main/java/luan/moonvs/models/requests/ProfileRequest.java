package luan.moonvs.models.requests;

import luan.moonvs.models.entities.Content;
import luan.moonvs.models.enums.Privacy;

public record ProfileRequest(String biography,
                             boolean isPrivate,
                             Content favoriteMovie,
                             Content favoriteSeries) { }
