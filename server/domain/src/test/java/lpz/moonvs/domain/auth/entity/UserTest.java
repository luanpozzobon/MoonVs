package lpz.moonvs.domain.auth.entity;

import lpz.moonvs.domain.auth.contracts.IPasswordEncryptor;
import lpz.moonvs.domain.auth.valueobject.Email;
import lpz.moonvs.domain.auth.valueobject.Password;
import lpz.moonvs.domain.seedwork.exception.DomainValidationException;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.valueobject.Id;
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
public class UserTest {
    private static final String VALID_EMAIL = "luanpozobon@gmail.com";
    private static final String VALID_PASSWORD = "r@w_Passw0rd";
    private static final String ENCRYPTED_PASSWORD = "encrypted_password";
    private static final String VALID_USERNAME = "luanpozzobon";

    @Mock
    private IPasswordEncryptor encryptor;

    private Email email;
    private Password password;
    private NotificationHandler handler;

    @BeforeEach
    public void setUp() {
        when(this.encryptor.encrypt(anyString())).thenReturn(ENCRYPTED_PASSWORD);

        this.handler = NotificationHandler.create();
        this.email = Email.create(this.handler, VALID_EMAIL);
        this.password = Password.create(this.encryptor, this.handler, VALID_PASSWORD);
    }

    @Test
    public void shouldCreateUserSuccessfully() {
        final User user = assertDoesNotThrow(() -> User.create(this.handler, VALID_USERNAME, this.email, this.password));

        assertNotNull(user);
        assertNotNull(user.getId());

        assertEquals(VALID_USERNAME, user.getUsername());
        assertEquals(this.email, user.getEmail());
        assertEquals(this.password, user.getPassword());

        assertFalse(this.handler.hasError());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    public void shouldThrowDomainValidationExceptionWhenUsernameIsNullOrBlank(String invalidUsername) {
        final var exception = assertThrows(DomainValidationException.class, () ->
                User.create(this.handler, invalidUsername, this.email, this.password)
        );

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals("username", this.handler.getErrors().getFirst().getKey());
        assertEquals("The username must be filled in.", this.handler.getErrors().getFirst().getMessage());

        assertEquals("Failed to create an user.", exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @Test
    public void shouldThrowDomainValidationExceptionWhenUsernameIsTooShort() {
        final var exception = assertThrows(DomainValidationException.class, () -> {
            User.create(this.handler, "lpz", this.email, this.password);
        });

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals("username", this.handler.getErrors().getFirst().getKey());
        assertEquals("The username must have at least 4 characters.", this.handler.getErrors().getFirst().getMessage());

        assertEquals("Failed to create an user.", exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @Test
    public void shouldThrowDomainValidationExceptionWhenUsernameContainsNonAlphanumeric() {
        final var exception = assertThrows(DomainValidationException.class, () -> {
            User.create(this.handler, "luanpozzobon@", this.email, this.password);
        });

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals("username", this.handler.getErrors().getFirst().getKey());
        assertEquals("The username must include only alphanumeric characters", this.handler.getErrors().getFirst().getMessage());

        assertEquals("Failed to create an user.", exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @Test
    public void shouldLoadExistingUser() {
        final Id<User> id = Id.unique();
        final Email email = Email.load(VALID_EMAIL);
        final Password password = Password.encrypted(ENCRYPTED_PASSWORD);

        final User user = User.load(id, VALID_USERNAME, email, password);

        assertNotNull(user);

        assertFalse(this.handler.hasError());

        assertEquals(id, user.getId());
        assertEquals(VALID_USERNAME, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
    }
}