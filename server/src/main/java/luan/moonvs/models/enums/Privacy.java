package luan.moonvs.models.enums;

import lombok.Getter;

@Getter
public enum Privacy {
    PRIVATE(true),
    PUBLIC (false);

    private final boolean privacy;

    Privacy(boolean privacy) {
        this.privacy = privacy;
    }
}
