package lpz.moonvs.domain.auth.entity;

import lpz.moonvs.domain.auth.validation.UserValidator;
import lpz.moonvs.domain.auth.valueobject.Email;
import lpz.moonvs.domain.auth.valueobject.Password;
import lpz.moonvs.domain.seedwork.exception.DomainValidationException;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.valueobject.Id;


public final class User {
    private final Id<User> id;
    private String username;
    private Email email;
    private Password password;

    public Id<User> getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    private User(final NotificationHandler handler,
                 final Id<User> id,
                 final String username,
                 final Email email,
                 final Password password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;

        this.selfValidate(handler);
    }

    public static User create(final NotificationHandler handler,
                              final String username,
                              final Email email,
                              final Password password) {
        return new User(handler, Id.unique(), username, email, password);
    }

    public static User load(final Id<User> id,
                            final String username,
                            final Email email,
                            final Password password) {
        return new User(null, id, username, email, password);
    }

    private void selfValidate(final NotificationHandler handler) {
        if (handler == null) return;

        new UserValidator(handler).validate(this);

        if (handler.hasError())
            throw new DomainValidationException("Failed to create an user.", handler.getErrors());
    }
}
