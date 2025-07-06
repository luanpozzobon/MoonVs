package lpz.moonvs.infra.auth.repository;

import lpz.moonvs.domain.auth.contracts.IUserRepository;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.infra.auth.entity.UserEntity;
import lpz.moonvs.infra.auth.mapper.UserMapper;
import lpz.moonvs.infra.exception.DataAccessException;
import lpz.utils.dao.Result;
import lpz.utils.dao.postgresql.CRUDBuilderFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepository implements IUserRepository {
    private final DataSource dataSource;

    public UserRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User save(final User user) {
        final UserEntity entity = UserMapper.from(user);

        try (final var connection = this.dataSource.getConnection()) {
            return UserMapper.to(
                    new CRUDBuilderFactory(connection)
                            .insert(UserEntity.class)
                            .returning()
                            .execute(entity).entities().get(0)
            );
        } catch (final SQLException e) {
            throw DataAccessException.save(e);
        }
    }

    private Result<UserEntity> findBy(final String field,
                                      final Object value) {
        try (final var connection = this.dataSource.getConnection()) {
            return new CRUDBuilderFactory(connection)
                    .select(UserEntity.class)
                    .where(field).equal(value)
                    .execute();
        } catch (final SQLException e) {
            throw DataAccessException.select(e);
        }
    }

    @Override
    public Optional<User> findById(final Id<User> id) {
        final List<UserEntity> entities = this.findBy(User.Schema.ID, UUID.fromString(id.getValue())).entities();

        return entities.stream().findFirst()
                .map(UserMapper::to);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        final List<UserEntity> entities = this.findBy(User.Schema.EMAIL, email).entities();

        return entities.stream().findFirst()
                .map(UserMapper::to);
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        final List<UserEntity> entities = this.findBy(User.Schema.USERNAME, username).entities();

        return entities.stream().findFirst()
                .map(UserMapper::to);
    }

    @Override
    public int update(final User user) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public int delete(final User user) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
