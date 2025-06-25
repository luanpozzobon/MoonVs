package lpz.moonvs.application.auth.usecase;

import lpz.moonvs.application.auth.command.RegisterCommand;
import lpz.moonvs.application.auth.output.RegisterOutput;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.auth.exception.UserAlreadyExistsException;
import lpz.moonvs.domain.auth.contracts.IPasswordEncryptor;
import lpz.moonvs.domain.auth.contracts.IUserRepository;
import lpz.moonvs.domain.auth.valueobject.Email;
import lpz.moonvs.domain.auth.valueobject.Password;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;

public class RegisterUseCase {
    private static final String EXISTING_USER = "There is already an user registered with this info";

    private final IUserRepository repository;
    private final IPasswordEncryptor encryptor;

    public RegisterUseCase(final IUserRepository repository,
                           final IPasswordEncryptor encryptor) {
        this.repository = repository;
        this.encryptor = encryptor;
    }

    public RegisterOutput execute(final RegisterCommand command) {
        final var handler = NotificationHandler.create();
        if (this.repository.findByEmail(command.email()).isPresent())
            handler.addError(new Notification("email", command.email()));
        if (this.repository.findByUsername(command.username()).isPresent())
            handler.addError(new Notification("username", command.username()));

        if (handler.hasError())
            throw new UserAlreadyExistsException(EXISTING_USER, handler.getErrors());

        final Email email = Email.create(handler, command.email());
        final Password password = Password.create(this.encryptor, handler, command.password());
        final User user = User.create(handler, command.username(), email, password);

        return RegisterOutput.from(this.repository.save(user));
    }
}
