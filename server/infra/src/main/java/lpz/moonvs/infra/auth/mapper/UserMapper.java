package lpz.moonvs.infra.auth.mapper;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.auth.valueobject.Email;
import lpz.moonvs.domain.auth.valueobject.Password;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.infra.auth.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public abstract class UserMapper {
    public static UserEntity from(final User user) {
        if (user == null) return null;

        final UserEntity entity = new UserEntity();
        Optional.ofNullable(user.getId())
                .map(id -> UUID.fromString(id.getValue()))
                .ifPresent(entity::setId);

        entity.setUsername(user.getUsername());

        Optional.ofNullable(user.getEmail())
                .map(Email::getValue)
                .ifPresent(entity::setEmail);

        Optional.ofNullable(user.getPassword())
                .map(Password::getValue)
                .ifPresent(entity::setPassword);

        return entity;
    }

    public static User to(final UserEntity entity) {
        final Id<User> id = Id.from(entity.getId());
        final Email email = Email.load(entity.getEmail());
        final Password password = Password.encrypted(entity.getPassword());
        return User.load(id, entity.getUsername(), email, password);
    }
}
