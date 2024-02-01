package luan.moonvs.models.enums;

import lombok.Getter;

@Getter
public enum ContentType {
    MOVIE("movie"),
    TV("tv");

    private final String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }
}
