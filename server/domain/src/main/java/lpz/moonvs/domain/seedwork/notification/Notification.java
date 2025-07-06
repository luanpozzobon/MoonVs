package lpz.moonvs.domain.seedwork.notification;

import java.util.Arrays;

public record Notification(String key, String message, Object... args) {
    public interface Schema {
        String NULL_OR_BLANK = "error.common.null-or-blank";
        String MAX_LENGTH = "error.common.max-length";
        String MIN_LENGTH = "error.common.min-length";
        String ALREADY_EXISTS = "error.common.already-exists";
    }

    public static Notification nullOrBlank(final String key,
                                           final Object... args) {
        return new Notification(key, Schema.NULL_OR_BLANK, args);
    }

    public static Notification minLength(final String key,
                                         final Object... args) {
        return new Notification(key, Schema.MIN_LENGTH, args);
    }

    public static Notification maxLength(final String key,
                                         final Object... args) {
        return new Notification(key, Schema.MAX_LENGTH, args);
    }

    public static Notification alreadyExists(final String key,
                                             final Object... args) {
        return new Notification(key, Schema.ALREADY_EXISTS, args);
    }

    public static Notification of(final String key,
                                  final String context,
                                  final Object... args) {
        return new Notification(key, context, args);
    }

    @Override
    public String toString() {
        return "Notification{field='%s', error-key='%s', args=%s}".formatted(key, message, Arrays.toString(args));
    }
}
