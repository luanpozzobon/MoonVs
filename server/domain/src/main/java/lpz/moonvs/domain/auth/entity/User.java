package lpz.moonvs.domain.auth.entity;

import lpz.moonvs.domain.auth.validation.UserValidator;
import lpz.moonvs.domain.auth.valueobject.Email;
import lpz.moonvs.domain.auth.valueobject.Password;
import lpz.moonvs.domain.seedwork.exception.DomainValidationException;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.valueobject.Id;

import java.util.Objects;

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

    private User(final Id<User> id,
                 final String username,
                 final Email email,
                 final Password password) {
        final String message = "'%s' cannot be null.";
        this.id = Objects.requireNonNull(id, String.format(message, UserSchema.ID));
        this.username = Objects.requireNonNull(username, String.format(message, UserSchema.USERNAME));
        this.email = Objects.requireNonNull(email, String.format(message, UserSchema.EMAIL));
        this.password = Objects.requireNonNull(password, String.format(message, UserSchema.PASSWORD));

    }

    public static User create(final NotificationHandler handler,
                              final String username,
                              final Email email,
                              final Password password) {
        return new User(Id.unique(), username, email, password)
                .selfValidate(handler);
    }

    public static User load(final Id<User> id,
                            final String username,
                            final Email email,
                            final Password password) {
        return new User(id, username, email, password);
    }

    private User selfValidate(final NotificationHandler handler) {
        Objects.requireNonNull(handler, "Notification handler cannot be null");
        new UserValidator(handler).validate(this);

        if (handler.hasError()) {
            throw new DomainValidationException(handler.getErrors());
        }

        return this;
    }
}