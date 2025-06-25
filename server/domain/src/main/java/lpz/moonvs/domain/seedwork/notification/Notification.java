package lpz.moonvs.domain.seedwork.notification;

public class Notification {
    private final String key;
    private final String message;

    public Notification(final String key, final String message) {
        this.key = key;
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Notification{field='%s', message='%s'}".formatted(key, message);
    }
}
