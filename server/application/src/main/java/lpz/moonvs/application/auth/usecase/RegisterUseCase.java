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
    public final static String EMAIL_KEY = "email";
    public final static String USERNAME_KEY = "username";
    public final static String ALREADY_EXISTS_ERROR_KEY = "error.common.already-exists";

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
                    EMAIL_KEY, ALREADY_EXISTS_ERROR_KEY, User.RESOURCE_KEY, EMAIL_KEY, command.email()
            ));
        if (command.username() != null && this.repository.findByUsername(command.username()).isPresent())
            handler.addError(new Notification(
                    USERNAME_KEY, ALREADY_EXISTS_ERROR_KEY, User.RESOURCE_KEY, USERNAME_KEY, command.username()
            ));

        if (handler.hasError())
            throw new UserAlreadyExistsException(handler.getErrors());
    }
}
