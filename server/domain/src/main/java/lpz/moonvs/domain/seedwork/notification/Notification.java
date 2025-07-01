package lpz.moonvs.domain.seedwork.notification;

import java.util.Arrays;

public class Notification {
    private final String key;
    private final String message;
    private final transient Object[] args;

    public Notification(final String key,
                        final String message,
                        final Object... args) {
        this.key = key;
        this.message = message;
        this.args = args;
    }

    public String getKey() {
        return key;
    }

    public String getMessage() {
        return message;
    }

    public Object[] getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return "Notification{field='%s', error-key='%s', args=%s}".formatted(key, message, Arrays.toString(args));
    }
}
