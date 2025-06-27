package lpz.moonvs.application.auth.usecase;

import lpz.moonvs.application.auth.command.RegisterCommand;
import lpz.moonvs.application.auth.output.RegisterOutput;
import lpz.moonvs.domain.auth.contracts.IPasswordEncryptor;
import lpz.moonvs.domain.auth.contracts.IUserRepository;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.auth.exception.UserAlreadyExistsException;
import lpz.moonvs.domain.auth.valueobject.Email;
import lpz.moonvs.domain.auth.valueobject.Password;
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
public class RegisterUseCaseTest {
    private static final String VALID_EMAIL = "luanpozzobon@gmail.com";
    private static final String VALID_USERNAME = "luanpozzobon";
    private static final String VALID_PASSWORD = "M00n_Vs.";

    @Mock
    private IUserRepository repository;

    @Mock
    private IPasswordEncryptor encryptor;

    @InjectMocks
    private RegisterUseCase useCase;


    @Test
    public void shouldExecuteSuccessfully() {
        final Email email = Email.load(VALID_EMAIL);
        final Password password = Password.encrypted(VALID_PASSWORD);
        final User anUser = User.create(null, VALID_USERNAME, email, password);

        when(this.repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(this.repository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(this.repository.save(any(User.class))).thenReturn(anUser);

        final RegisterOutput output = assertDoesNotThrow(() -> this.useCase.execute(
                new RegisterCommand(VALID_EMAIL, VALID_USERNAME, VALID_PASSWORD)
        ));

        assertNotNull(output);
        assertNotNull(output.id());
        assertEquals("luanpozzobon@gmail.com", output.email());
        assertEquals("luanpozzobon", output.username());
    }

    @Test
    public void shouldThrowUserAlreadyExistsExceptionWhenEmailExists() {
        final User existingUser = User.load(null, null, null, null);

        when(this.repository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));
        when(this.repository.findByUsername(anyString())).thenReturn(Optional.empty());

        final var exception = assertThrows(UserAlreadyExistsException.class, () ->
                this.useCase.execute(new RegisterCommand(VALID_EMAIL, VALID_USERNAME, VALID_PASSWORD))
        );

        assertEquals("There is already an user registered with this info", exception.getMessage());
        assertEquals(1, exception.getErrors().size());
        assertEquals("email", exception.getErrors().getFirst().getKey());
        assertEquals(VALID_EMAIL, exception.getErrors().getFirst().getMessage());
    }

    @Test
    public void shouldThrowUserAlreadyExistsExceptionWhenUsernameExists() {
        final User existingUser = User.load(null, null, null, null);

        when(this.repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(this.repository.findByUsername(anyString())).thenReturn(Optional.of(existingUser));

        final var exception = assertThrows(UserAlreadyExistsException.class, () ->
                this.useCase.execute(new RegisterCommand(VALID_EMAIL, VALID_USERNAME, VALID_PASSWORD))
        );

        assertEquals("There is already an user registered with this info", exception.getMessage());
        assertEquals(1, exception.getErrors().size());
        assertEquals("username", exception.getErrors().getFirst().getKey());
        assertEquals(VALID_USERNAME, exception.getErrors().getFirst().getMessage());
    }
}
