package lpz.moonvs.domain.auth.valueobject;

import lpz.moonvs.domain.auth.entity.UserSchema;
import lpz.moonvs.domain.auth.validation.EmailValidator;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {
    private static final String VALID_EMAIL = "luanpozzobon@gmail.com";

    private NotificationHandler handler;

    @BeforeEach
    void setUp() {
        this.handler = NotificationHandler.create();
    }

    @Test
    void shouldCreateEmailSuccessfully() {
        final Email email = Email.create(this.handler, VALID_EMAIL);

        assertNotNull(email);
        assertEquals(VALID_EMAIL, email.getValue());

        assertFalse(this.handler.hasError());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    void shouldAddNotificationWhenEmailIsNullOrBlank(final String invalidEmail) {
        Email.create(this.handler, invalidEmail);

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals(UserSchema.EMAIL, this.handler.getErrors().getFirst().key());
        assertEquals(Notification.NULL_OR_BLANK, this.handler.getErrors().getFirst().message());
    }

    @ParameterizedTest
    @ValueSource(strings = {"example", "@example", "@example.com", "example@example", "example.com", "example@", "example@example."})
    void shouldAddNotificationWhenEmailFormatIsInvalid(final String invalidEmail) {
        Email.create(this.handler, invalidEmail);

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals(UserSchema.EMAIL, this.handler.getErrors().getFirst().key());
        assertEquals(EmailValidator.INVALID, this.handler.getErrors().getFirst().message());
    }
}
