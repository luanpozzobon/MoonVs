package luan.moonvs.models.enums;

public enum SearchType {
    INTERNAL("internal"),
    EXTERNAL("external");

    private final String value;

    SearchType(String value) {
        this.value = value;
    }
}
