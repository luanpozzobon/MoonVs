package lpz.moonvs.domain.seedwork.valueobject;

import lpz.moonvs.domain.seedwork.exception.DomainValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class IdTest {

    @Test
    public void shouldCreateUnique() {
        final Id<?> id = assertDoesNotThrow(() -> Id.unique());

        assertNotNull(id);
        assertNotNull(id.getValue());
        assertDoesNotThrow(() -> UUID.fromString(id.getValue()));
    }

    @Test
    public void shouldCreateFromUUID() {
        final Id<?> id = assertDoesNotThrow(() -> Id.from(UUID.randomUUID()));

        assertNotNull(id);
        assertNotNull(id.getValue());
        assertDoesNotThrow(() -> UUID.fromString(id.getValue()));
    }

    @Test
    public void shouldCreateFromString() {
        final Id<?> id = assertDoesNotThrow(() -> Id.from("id"));

        assertNotNull(id);
        assertNotNull(id.getValue());
    }

    @Test
    public void shouldCreateFromLong() {
        final Id<?> id = assertDoesNotThrow(() -> Id.from(1L));

        assertNotNull(id);
        assertNotNull(id.getValue());
        assertDoesNotThrow(() -> Long.parseLong(id.getValue()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    public void shouldThrowDomainValidationExceptionWhenNullOrBlank(String invalidId) {
        final var exception = assertThrows(DomainValidationException.class, () ->
                Id.from(invalidId)
        );

        assertEquals("Id cannot be null or empty", exception.getMessage());
    }
}
