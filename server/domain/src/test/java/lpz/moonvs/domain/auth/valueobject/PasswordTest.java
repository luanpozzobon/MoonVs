package lpz.moonvs.domain.auth.valueobject;


import lpz.moonvs.domain.auth.contracts.IPasswordEncryptor;
import lpz.moonvs.domain.auth.entity.UserSchema;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordTest {
    private static final String VALID_PASSWORD = "r@w_Passw0rd";
    private static final String ENCRYPTED_PASSWORD = "encrypted_password";

    @Mock
    private IPasswordEncryptor encryptor;

    private NotificationHandler handler;

    @BeforeEach
    void setUp() {
        this.handler = NotificationHandler.create();
    }

    @Test
    void shouldCreatePasswordSuccessfully() {
        when(this.encryptor.encrypt(anyString())).thenReturn(ENCRYPTED_PASSWORD);
        final Password password = Password.create(this.encryptor, this.handler, VALID_PASSWORD);

        assertNotNull(password);
        assertNotNull(password.getValue());
        assertNull(password.getRaw());

        assertEquals(ENCRYPTED_PASSWORD, password.getValue());

        assertFalse(this.handler.hasError());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    void shouldAddNotificationWhenPasswordIsNullOrBlank(final String invalidPassword) {
        Password.create(this.encryptor, this.handler, invalidPassword);

        assertTrue(this.handler.hasError());

        assertEquals(1, this.handler.getErrors().size());
        assertEquals(UserSchema.PASSWORD, this.handler.getErrors().getFirst().key());
        assertEquals(Notification.NULL_OR_BLANK, this.handler.getErrors().getFirst().message());
    }

    @ParameterizedTest
    @CsvSource({
            "'aA@1', 'error.common.min-length'",
            "'password@1', 'error.user.password.uppercase'",
            "'PASSWORD@1', 'error.user.password.lowercase'",
            "'Password@', 'error.user.password.numeric'",
            "'Password1', 'error.user.password.special'"
    })
    void shouldAddNotificationWhenPasswordIsInvalid(final String invalidPassword,
                                                    final String errorMessage) {
        Password.create(this.encryptor, this.handler, invalidPassword);
        assertTrue(this.handler.hasError());

        assertEquals(1, this.handler.getErrors().size());
        assertEquals(UserSchema.PASSWORD, this.handler.getErrors().getFirst().key());
        assertEquals(errorMessage, this.handler.getErrors().getFirst().message());
    }

    @Test
    void shouldLoadEncryptedPassword() {
        final Password password = Password.encrypted(ENCRYPTED_PASSWORD);

        assertFalse(this.handler.hasError());

        assertNotNull(password);
        assertNull(password.getRaw());
        assertEquals(ENCRYPTED_PASSWORD, password.getValue());
    }
}
