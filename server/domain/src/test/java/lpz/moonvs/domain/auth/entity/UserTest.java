package lpz.moonvs.domain.auth.entity;

import lpz.moonvs.domain.auth.contracts.IPasswordEncryptor;
import lpz.moonvs.domain.auth.validation.UserValidator;
import lpz.moonvs.domain.auth.valueobject.Email;
import lpz.moonvs.domain.auth.valueobject.Password;
import lpz.moonvs.domain.seedwork.exception.DomainValidationException;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTest {
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
    void setUp() {
        when(this.encryptor.encrypt(anyString())).thenReturn(ENCRYPTED_PASSWORD);

        this.handler = NotificationHandler.create();
        this.email = Email.create(this.handler, VALID_EMAIL);
        this.password = Password.create(this.encryptor, this.handler, VALID_PASSWORD);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        final User user = assertDoesNotThrow(() -> User.create(this.handler, VALID_USERNAME, this.email, this.password));

        assertNotNull(user);
        assertNotNull(user.getId());

        assertEquals(VALID_USERNAME, user.getUsername());
        assertEquals(this.email, user.getEmail());
        assertEquals(this.password, user.getPassword());

        assertFalse(this.handler.hasError());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    void shouldThrowDomainValidationExceptionWhenUsernameIsBlank(String invalidUsername) {
        final var exception = assertThrows(DomainValidationException.class, () ->
                User.create(this.handler, invalidUsername, this.email, this.password)
        );

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals(UserSchema.USERNAME, this.handler.getErrors().getFirst().key());
        assertEquals(Notification.NULL_OR_BLANK, this.handler.getErrors().getFirst().message());

        assertEquals(DomainValidationException.ERROR_KEY, exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @Test
    void shouldThrowDomainValidationExceptionWhenUsernameIsTooShort() {
        final var exception = assertThrows(DomainValidationException.class, () -> {
            User.create(this.handler, "lpz", this.email, this.password);
        });

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals(UserSchema.USERNAME, this.handler.getErrors().getFirst().key());
        assertEquals(Notification.MIN_LENGTH, this.handler.getErrors().getFirst().message());

        assertEquals(DomainValidationException.ERROR_KEY, exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @Test
    void shouldThrowDomainValidationExceptionWhenUsernameContainsNonAlphanumeric() {
        final var exception = assertThrows(DomainValidationException.class, () -> {
            User.create(this.handler, "luanpozzobon@", this.email, this.password);
        });

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals(UserSchema.USERNAME, this.handler.getErrors().getFirst().key());
        assertEquals(UserValidator.INVALID_CHARACTERS, this.handler.getErrors().getFirst().message());

        assertEquals(DomainValidationException.ERROR_KEY, exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @Test
    void shouldLoadExistingUser() {
        final Id<User> id = Id.unique();
        final Email anEmail = Email.load(VALID_EMAIL);
        final Password aPassword = Password.encrypted(ENCRYPTED_PASSWORD);

        final User user = User.load(id, VALID_USERNAME, anEmail, aPassword);

        assertNotNull(user);

        assertFalse(this.handler.hasError());

        assertEquals(id, user.getId());
        assertEquals(VALID_USERNAME, user.getUsername());
        assertEquals(anEmail, user.getEmail());
        assertEquals(aPassword, user.getPassword());
    }
}