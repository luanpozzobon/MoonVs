package lpz.moonvs.infra.playlist.mapper;

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
    private static final String anUsername = "luanpozzobon";
    private static final String anEmail = "luanpozzobon@gmail.com";
    private static final String aPassword = "M00n_Vs.";

    @Test
    void shouldMapFromDomainSuccessfully() {
        final Id<User> id = Id.unique();
        final Email email = Email.load(anEmail);
        final Password password = Password.encrypted(aPassword);
        final User anUser = User.load(id, anUsername, email, password);

        final UserEntity entity = assertDoesNotThrow(() ->
                UserMapper.from(anUser)
        );

        assertNotNull(entity);

        assertEquals(UUID.fromString(id.getValue()), entity.getId());
        assertEquals(anUsername, entity.getUsername());
        assertEquals(anEmail, entity.getEmail());
        assertEquals(aPassword, entity.getPassword());
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
        entity.setUsername(anUsername);
        entity.setEmail(anEmail);
        entity.setPassword(aPassword);

        final User user = assertDoesNotThrow(() ->
                UserMapper.to(entity)
        );

        assertNotNull(user);
        assertEquals(id, UUID.fromString(user.getId().getValue()));
        assertEquals(anUsername, user.getUsername());
        assertEquals(anEmail, user.getEmail().getValue());
        assertEquals(aPassword, user.getPassword().getValue());
    }
}
