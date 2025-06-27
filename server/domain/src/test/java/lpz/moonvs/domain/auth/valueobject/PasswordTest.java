package lpz.moonvs.domain.auth.valueobject;


import lpz.moonvs.domain.auth.contracts.IPasswordEncryptor;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PasswordTest {
    private static final String VALID_PASSWORD = "r@w_Passw0rd";
    private static final String ENCRYPTED_PASSWORD = "encrypted_password";

    @Mock
    private IPasswordEncryptor encryptor;

    private NotificationHandler handler;

    @BeforeEach
    public void setUp() {
        this.handler = NotificationHandler.create();
    }

    @Test
    public void shouldCreatePasswordSuccessfully() {
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
    public void shouldAddNotificationWhenPasswordIsNullOrBlank(final String invalidPassword) {
        Password.create(this.encryptor, this.handler, invalidPassword);

        assertTrue(this.handler.hasError());

        assertEquals(1, this.handler.getErrors().size());
        assertEquals("password", this.handler.getErrors().getFirst().getKey());
        assertEquals("The password must be filled in.", this.handler.getErrors().getFirst().getMessage());
    }

    @Test
    public void shouldAddNotificationWhenPasswordIsTooShort() {
        Password.create(this.encryptor, this.handler, "aA@1");

        assertTrue(this.handler.hasError());

        assertEquals(1, this.handler.getErrors().size());
        assertEquals("password", this.handler.getErrors().getFirst().getKey());
        assertEquals("The password must include at least 8 characters.", this.handler.getErrors().getFirst().getMessage());
    }

    @Test
    public void shouldAddNotificationWhenPasswordHasNoUppercase() {
        Password.create(this.encryptor, this.handler, "password@1");

        assertTrue(this.handler.hasError());

        assertEquals(1, this.handler.getErrors().size());
        assertEquals("password", this.handler.getErrors().getFirst().getKey());
        assertEquals("The password must include at least 1 uppercase letter.", this.handler.getErrors().getFirst().getMessage());
    }

    @Test
    public void shouldAddNotificationWhenPasswordHasNoLowercase() {
        Password.create(this.encryptor, this.handler, "PASSWORD@1");

        assertTrue(this.handler.hasError());

        assertEquals(1, this.handler.getErrors().size());
        assertEquals("password", this.handler.getErrors().getFirst().getKey());
        assertEquals("The password must include at least 1 lowercase letter.", this.handler.getErrors().getFirst().getMessage());
    }

    @Test
    public void shouldAddNotificationWhenPasswordHasNoNumeric() {
        Password.create(this.encryptor, this.handler, "Password@");

        assertTrue(this.handler.hasError());

        assertEquals(1, this.handler.getErrors().size());
        assertEquals("password", this.handler.getErrors().getFirst().getKey());
        assertEquals("The password must include at least 1 numeric character.", this.handler.getErrors().getFirst().getMessage());
    }

    @Test
    public void shouldAddNotificationWhenPasswordHasNoLSpecial() {
        Password.create(this.encryptor, this.handler, "Password1");

        assertTrue(this.handler.hasError());

        assertEquals(1, this.handler.getErrors().size());
        assertEquals("password", this.handler.getErrors().getFirst().getKey());
        assertEquals("The password must include at least 1 special character.", this.handler.getErrors().getFirst().getMessage());
    }

    @Test
    public void shouldLoadEncryptedPassword() {
        final Password password = Password.encrypted(ENCRYPTED_PASSWORD);

        assertFalse(this.handler.hasError());

        assertNotNull(password);
        assertNull(password.getRaw());
        assertEquals(ENCRYPTED_PASSWORD, password.getValue());
    }
}
