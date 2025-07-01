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

public class RegisterUseCase {
    private final IUserRepository repository;
    private final IPasswordEncryptor encryptor;

    public RegisterUseCase(final IUserRepository repository,
                           final IPasswordEncryptor encryptor) {
        this.repository = repository;
        this.encryptor = encryptor;
    }

    public RegisterOutput execute(final RegisterCommand command) {
        final var handler = NotificationHandler.create();
        this.validateIfExists(handler, command);

        final Email email = Email.create(handler, command.email());
        final Password password = Password.create(this.encryptor, handler, command.password());
        final User user = User.create(handler, command.username(), email, password);

        return RegisterOutput.from(this.repository.save(user));
    }

    private void validateIfExists(final NotificationHandler handler,
                                  final RegisterCommand command) {
        if (command.email() != null && this.repository.findByEmail(command.email()).isPresent())
            handler.addError(new Notification(
                    "email", "error.user.already-exists", "E-mail", command.email()));
        if (command.username() != null && this.repository.findByUsername(command.username()).isPresent())
            handler.addError(new Notification(
                    "username", "error.user.already-exists", "Username", command.username()));

        if (handler.hasError())
            throw new UserAlreadyExistsException("error.user.register", handler.getErrors());
    }
}
