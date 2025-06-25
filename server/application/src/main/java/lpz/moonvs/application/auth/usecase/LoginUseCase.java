package lpz.moonvs.application.auth.usecase;

import lpz.moonvs.application.auth.command.LoginCommand;
import lpz.moonvs.application.auth.output.LoginOutput;
import lpz.moonvs.domain.auth.contracts.IPasswordEncryptor;
import lpz.moonvs.domain.auth.contracts.IUserRepository;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.auth.exception.UserDoesNotExistsException;

import java.util.Optional;

public class LoginUseCase {
    private final static String NON_EXISTING_USER = "There is no user registered with these credentials.";

    private final IUserRepository userRepository;
    private final IPasswordEncryptor passwordEncryptor;

    public LoginUseCase(final IUserRepository userRepository,
                        final IPasswordEncryptor passwordEncryptor) {
        this.userRepository = userRepository;
        this.passwordEncryptor = passwordEncryptor;
    }

    public LoginOutput execute(final LoginCommand command) {
        final Optional<User> anUser = this.userRepository.findByUsername(command.username());
        if (anUser.isEmpty())
            throw new UserDoesNotExistsException(NON_EXISTING_USER);
        if (!passwordEncryptor.matches(command.password(), anUser.get().getPassword().getValue()))
            throw new UserDoesNotExistsException(NON_EXISTING_USER);

        return LoginOutput.from(anUser.get());
    }
}
