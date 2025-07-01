package lpz.moonvs.application.auth.usecase;

import lpz.moonvs.application.auth.command.LoginCommand;
import lpz.moonvs.application.auth.output.LoginOutput;
import lpz.moonvs.domain.auth.contracts.IPasswordEncryptor;
import lpz.moonvs.domain.auth.contracts.IUserRepository;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.auth.exception.UserDoesNotExistsException;
import lpz.moonvs.domain.auth.valueobject.Email;
import lpz.moonvs.domain.auth.valueobject.Password;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {
    private static final String VALID_USERNAME = "luanpozzobon";
    private static final String VALID_PASSWORD = "M00n_Vs.";
    private static LoginCommand command;

    @BeforeAll
    static void init() {
        command = new LoginCommand(VALID_USERNAME, VALID_PASSWORD);
    }

    @Mock
    private IUserRepository repository;

    @Mock
    private IPasswordEncryptor encryptor;

    @InjectMocks
    private LoginUseCase useCase;

    @Test
    void shouldExecuteSuccessfully() {
        final Email email = Email.load("luanpozzobon@gmail.com");
        final Password password = Password.encrypted(VALID_PASSWORD);
        final User anUser = User.load(Id.unique(), VALID_USERNAME, email, password);

        when(this.repository.findByUsername(anyString())).thenReturn(Optional.of(anUser));
        when(this.encryptor.matches(anyString(), anyString())).thenReturn(true);

        LoginOutput output = assertDoesNotThrow(() ->
                this.useCase.execute(command)
        );

        assertNotNull(output);
        assertEquals(VALID_USERNAME, output.username());
    }

    @Test
    void shouldThrowUserDoesNotExistsExceptionWhenUsernameDontExist() {
        when(this.repository.findByUsername(anyString())).thenReturn(Optional.empty());

        final var exception = assertThrows(UserDoesNotExistsException.class, () ->
                this.useCase.execute(command)
        );

        assertEquals(UserDoesNotExistsException.ERROR_KEY, exception.getMessage());
    }

    @Test
    void shouldThrowUserDoesNotExistsExceptionWhenPasswordIsDifferent() {
        final Password password = Password.encrypted(VALID_PASSWORD);
        final User anUser = User.load(Id.unique(), VALID_USERNAME, null, password);

        when(this.repository.findByUsername(anyString())).thenReturn(Optional.of(anUser));
        when(this.encryptor.matches(anyString(), anyString())).thenReturn(false);

        final var exception = assertThrows(UserDoesNotExistsException.class, () ->
                this.useCase.execute(command)
        );

        assertEquals(UserDoesNotExistsException.ERROR_KEY, exception.getMessage());
    }
}
