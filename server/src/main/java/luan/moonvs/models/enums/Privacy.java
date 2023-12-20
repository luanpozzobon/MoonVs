package luan.moonvs.models.enums;

public enum Privacy {
    PRIVATE(true),
    PUBLIC (false);

    private final boolean privacy;

    Privacy(boolean privacy) {
        this.privacy = privacy;
    }

    public Boolean getValue() {
        return this.privacy;
    }

}
