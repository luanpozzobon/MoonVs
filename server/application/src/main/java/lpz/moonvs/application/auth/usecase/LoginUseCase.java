package lpz.moonvs.application.auth.usecase;

import lpz.moonvs.application.auth.command.LoginCommand;
import lpz.moonvs.application.auth.output.LoginOutput;
import lpz.moonvs.domain.auth.contracts.IPasswordEncryptor;
import lpz.moonvs.domain.auth.contracts.IUserRepository;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.auth.exception.UserDoesNotExistsException;

public class LoginUseCase {
    private final IUserRepository userRepository;
    private final IPasswordEncryptor passwordEncryptor;

    public LoginUseCase(final IUserRepository userRepository,
                        final IPasswordEncryptor passwordEncryptor) {
        this.userRepository = userRepository;
        this.passwordEncryptor = passwordEncryptor;
    }

    public LoginOutput execute(final LoginCommand command) {
        final User user = this.findAndValidateUser(command.username());
        this.validatePassword(command.password(), user);

        return LoginOutput.from(user);
    }

    private User findAndValidateUser(final String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(UserDoesNotExistsException::new);
    }

    private void validatePassword(final String password,
                                  final User user) {
        final boolean passwordMatches = this.passwordEncryptor.matches(password, user.getPassword().getValue());

        if (!passwordMatches)
            throw new UserDoesNotExistsException();
    }
}
