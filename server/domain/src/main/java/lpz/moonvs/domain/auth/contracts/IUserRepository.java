package lpz.moonvs.domain.auth.contracts;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.seedwork.valueobject.Id;

import java.util.Optional;

public interface IUserRepository {
    User save(final User user);

    Optional<User> findById(final Id<User> id);

    Optional<User> findByEmail(final String email);

    Optional<User> findByUsername(final String username);

    int update(final User user);

    int delete(final User user);
}
