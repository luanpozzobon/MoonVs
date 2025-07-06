package lpz.moonvs.application.auth.usecase;

import lpz.moonvs.application.auth.command.RegisterCommand;
import lpz.moonvs.application.auth.output.RegisterOutput;
import lpz.moonvs.domain.auth.contracts.IPasswordEncryptor;
import lpz.moonvs.domain.auth.contracts.IUserRepository;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.auth.exception.UserAlreadyExistsException;
import lpz.moonvs.domain.auth.valueobject.Email;
import lpz.moonvs.domain.auth.valueobject.Password;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterUseCaseTest {
    private static final String VALID_EMAIL = "luanpozzobon@gmail.com";
    private static final String VALID_USERNAME = "luanpozzobon";
    private static final String VALID_PASSWORD = "M00n_Vs.";
    private static RegisterCommand command;

    @BeforeAll
    static void init() {
        command = new RegisterCommand(VALID_EMAIL, VALID_USERNAME, VALID_PASSWORD);
    }

    @Mock
    private IUserRepository repository;

    @Mock
    private IPasswordEncryptor encryptor;

    @InjectMocks
    private RegisterUseCase useCase;

    private Email email;
    private Password password;

    @BeforeEach
    void setUp() {
        this.email = Email.load(VALID_EMAIL);
        this.password = Password.encrypted(VALID_PASSWORD);
    }

    @Test
    void shouldExecuteSuccessfully() {
        final var handler = NotificationHandler.create();
        final User anUser = User.create(handler, VALID_USERNAME, this.email, this.password);

        when(this.repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(this.repository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(this.repository.save(any(User.class))).thenReturn(anUser);

        final RegisterOutput output = assertDoesNotThrow(() -> this.useCase.execute(command));

        assertNotNull(output);
        assertNotNull(output.id());
        assertEquals(VALID_EMAIL, output.email());
        assertEquals(VALID_USERNAME, output.username());
    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenEmailExists() {
        final Id<User> id = Id.unique();
        final User existingUser = User.load(id, VALID_USERNAME, this.email, this.password);

        when(this.repository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));
        when(this.repository.findByUsername(anyString())).thenReturn(Optional.empty());

        final var exception = assertThrows(UserAlreadyExistsException.class, () ->
                this.useCase.execute(command)
        );

        assertEquals(UserAlreadyExistsException.ERROR_KEY, exception.getMessage());
        assertEquals(1, exception.getErrors().size());
        assertEquals(User.Schema.EMAIL, exception.getErrors().getFirst().key());
        assertEquals(Notification.Schema.ALREADY_EXISTS, exception.getErrors().getFirst().message());
    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenUsernameExists() {
        final Id<User> id = Id.unique();
        final User existingUser = User.load(id, VALID_USERNAME, this.email, this.password);

        when(this.repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(this.repository.findByUsername(anyString())).thenReturn(Optional.of(existingUser));

        final var exception = assertThrows(UserAlreadyExistsException.class, () ->
                this.useCase.execute(command)
        );

        assertEquals(UserAlreadyExistsException.ERROR_KEY, exception.getMessage());
        assertEquals(1, exception.getErrors().size());
        assertEquals(User.Schema.USERNAME, exception.getErrors().getFirst().key());
        assertEquals(Notification.Schema.ALREADY_EXISTS, exception.getErrors().getFirst().message());
    }
}
