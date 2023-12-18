package luan.moonvs.models.enums;

import lombok.Getter;

@Getter
public enum ContentType {
    MOVIE("Movies"),
    TV("Series");

    private final String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }
}
