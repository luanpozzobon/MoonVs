package luan.moonvs.models.enums;

import lombok.Getter;

@Getter
public enum ContentType {
    MOVIE("Movies"),
    TV("Series");

    private String contentType;

    private ContentType(String contentType) {
        this.contentType = contentType;
    }
}
