package lpz.moonvs.infra.user.mapper;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.auth.valueobject.Email;
import lpz.moonvs.domain.auth.valueobject.Password;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.infra.auth.entity.UserEntity;
import lpz.moonvs.infra.auth.mapper.UserMapper;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    private static final String USERNAME = "luanpozzobon";
    private static final String EMAIL = "luanpozzobon@gmail.com";
    private static final String PASSWORD = "M00n_Vs.";

    @Test
    void shouldMapFromDomainSuccessfully() {
        final Id<User> id = Id.unique();
        final Email email = Email.load(EMAIL);
        final Password password = Password.encrypted(PASSWORD);
        final User anUser = User.load(id, USERNAME, email, password);

        final UserEntity entity = assertDoesNotThrow(() ->
                UserMapper.from(anUser)
        );

        assertNotNull(entity);

        assertEquals(UUID.fromString(id.getValue()), entity.getId());
        assertEquals(USERNAME, entity.getUsername());
        assertEquals(EMAIL, entity.getEmail());
        assertEquals(PASSWORD, entity.getPassword());
    }

    @Test
    void shouldReturnNull() {
        final UserEntity entity = assertDoesNotThrow(() ->
                UserMapper.from(null)
        );

        assertNull(entity);
    }

    @Test
    void shouldMapToDomainSuccessfully() {
        final UserEntity entity = new UserEntity();
        final UUID id = UUID.randomUUID();
        entity.setId(id);
        entity.setUsername(USERNAME);
        entity.setEmail(EMAIL);
        entity.setPassword(PASSWORD);

        final User user = assertDoesNotThrow(() ->
                UserMapper.to(entity)
        );

        assertNotNull(user);
        assertEquals(id, UUID.fromString(user.getId().getValue()));
        assertEquals(USERNAME, user.getUsername());
        assertEquals(EMAIL, user.getEmail().getValue());
        assertEquals(PASSWORD, user.getPassword().getValue());
    }
}
