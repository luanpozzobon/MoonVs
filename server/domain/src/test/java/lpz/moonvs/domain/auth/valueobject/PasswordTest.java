package lpz.moonvs.domain.auth.valueobject;


import lpz.moonvs.domain.auth.contracts.IPasswordEncryptor;
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
        assertNotNull(password.getRaw());
        assertNotNull(password.getValue());

        assertEquals(VALID_PASSWORD, password.getRaw());
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
        assertEquals("password", this.handler.getErrors().getFirst().getKey());
        assertEquals("The password must be filled in.", this.handler.getErrors().getFirst().getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "'aA@1', 'The password must include at least 8 characters.'",
            "'password@1', 'The password must include at least 1 uppercase letter.'",
            "'PASSWORD@1', 'The password must include at least 1 lowercase letter.'",
            "'Password@', 'The password must include at least 1 numeric character.'",
            "'Password1', 'The password must include at least 1 special character.'"
    })
    void shouldAddNotificationWhenPasswordIsInvalid(final String invalidPassword,
                                                    final String errorMessage) {
        Password.create(this.encryptor, this.handler, invalidPassword);
        assertTrue(this.handler.hasError());

        assertEquals(1, this.handler.getErrors().size());
        assertEquals("password", this.handler.getErrors().getFirst().getKey());
        assertEquals(errorMessage, this.handler.getErrors().getFirst().getMessage());
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
