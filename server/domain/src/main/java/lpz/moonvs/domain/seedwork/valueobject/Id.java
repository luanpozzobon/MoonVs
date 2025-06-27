package lpz.moonvs.domain.seedwork.valueobject;

import lpz.moonvs.domain.seedwork.exception.DomainValidationException;

import java.util.UUID;

public class Id<T> {
    private final String value;

    private Id(final String value) {
        if (value == null || value.isBlank())
            throw new DomainValidationException("Id cannot be null or empty", null);

        this.value = value;
    }

    public static <T> Id<T> unique() {
        return Id.from(UUID.randomUUID());
    }

    public static <T> Id<T> from(final UUID id) {
        return new Id<>(String.valueOf(id));
    }

    public static <T> Id<T> from(final String id) {
        return new Id<>(id);
    }

    public static <T> Id<T> from(final Long id) {
        return new Id<>(String.valueOf(id));
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public final boolean equals(final Object o) {
        if (!(o instanceof final Id<?> id)) return false;

        return this.getValue().equals(id.getValue());
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }
}
