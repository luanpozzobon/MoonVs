package lpz.moonvs.infra.user.repository;

import lpz.moonvs.domain.auth.contracts.IUserRepository;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.auth.valueobject.Email;
import lpz.moonvs.domain.auth.valueobject.Password;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.infra.MoonVsTest;
import lpz.moonvs.infra.exception.DataAccessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MoonVsTest.class)
@Testcontainers
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserRepositoryIntegrationTest {
    private static final String USERNAME = "luanpozzobon";
    private static final String EMAIL = "luanpozzobon@gmail.com";
    private static final String PASSWORD = "encrypted_password";

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15.13")
                    .withDatabaseName("mvs-test")
                    .withUsername("test-user")
                    .withPassword("test-password");

    @DynamicPropertySource
    static void postgreSQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private IUserRepository repository;

    @Test
    void shouldSaveAndRetrieveUser() {
        final Email email = Email.load(EMAIL);
        final Password password = Password.encrypted(PASSWORD);
        final User anUser = User.load(Id.unique(), USERNAME, email, password);

        final User savedUser = this.repository.save(anUser);
        final Optional<User> aRetrievedUser = this.repository.findById(savedUser.getId());

        assertNotNull(savedUser);
        assertTrue(aRetrievedUser.isPresent());
        final User retrievedUser = aRetrievedUser.get();

        assertEquals(savedUser.getId(), retrievedUser.getId());
        assertEquals(USERNAME, retrievedUser.getUsername());
        assertEquals(EMAIL, retrievedUser.getEmail().getValue());
        assertEquals(PASSWORD, retrievedUser.getPassword().getValue());
    }

    @Test
    void shouldThrowExceptionWhenEntityIsNull() {
        final var exception = assertThrows(NullPointerException.class, () ->
                this.repository.save(null)
        );

        assertEquals("The insertable entity must not be null.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSavingWithDuplicateId() {
        final Id<User> id = Id.unique();
        final Email email = Email.load(EMAIL);
        final Password password = Password.encrypted(PASSWORD);
        final User anUser = User.load(id, USERNAME, email, password);
        this.repository.save(anUser);

        final String username = "luanpozzobon1";
        final Email otherEmail = Email.load("luanpozzobon1@gmail.com");
        final User otherUser = User.load(id, username, otherEmail, password);

        final var exception = assertThrows(DataAccessException.class, () ->
                this.repository.save(otherUser)
        );

        assertEquals("Error saving user to database.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSavingWithDuplicateUsername() {
        final Email email = Email.load(EMAIL);
        final Password password = Password.encrypted(PASSWORD);
        final User anUser = User.load(Id.unique(), USERNAME, email, password);
        this.repository.save(anUser);

        final Email otherEmail = Email.load("luanpozzobon1@gmail.com");
        final User otherUser = User.load(Id.unique(), USERNAME, otherEmail, password);

        final var exception = assertThrows(DataAccessException.class, () ->
                this.repository.save(otherUser)
        );

        assertEquals("Error saving user to database.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSavingWithDuplicateEmail() {
        final Email email = Email.load(EMAIL);
        final Password password = Password.encrypted(PASSWORD);
        final User anUser = User.load(Id.unique(), USERNAME, email, password);
        this.repository.save(anUser);

        final String otherUsername = "luanpozzobon1";
        final User otherUser = User.load(Id.unique(), otherUsername, email, password);

        final var exception = assertThrows(DataAccessException.class, () ->
                this.repository.save(otherUser)
        );

        assertEquals("Error saving user to database.", exception.getMessage());
    }

    @Test
    void shouldFindById() {
        final Id<User> id = Id.unique();
        final Email email = Email.load(EMAIL);
        final Password password = Password.encrypted(PASSWORD);
        final User user = User.load(id, USERNAME, email, password);
        this.repository.save(user);

        final Optional<User> anUserOpt = assertDoesNotThrow(() ->
                this.repository.findById(id)
        );

        assertNotNull(anUserOpt);
        assertTrue(anUserOpt.isPresent());

        final User anUser = anUserOpt.get();
        assertNotNull(anUser);
        assertEquals(id, anUser.getId());
        assertEquals(USERNAME, anUser.getUsername());
        assertEquals(email.getValue(), anUser.getEmail().getValue());
        assertEquals(password.getValue(), anUser.getPassword().getValue());
    }

    @Test
    void shouldNotFindByDifferentId() {
        final Id<User> id = Id.unique();
        final Email email = Email.load(EMAIL);
        final Password password = Password.encrypted(PASSWORD);
        final User user = User.load(id, USERNAME, email, password);
        this.repository.save(user);

        final Optional<User> anUserOpt = assertDoesNotThrow(() ->
                this.repository.findById(Id.unique())
        );

        assertNotNull(anUserOpt);
        assertTrue(anUserOpt.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenLookingForNullId() {
        assertThrows(NullPointerException.class, () ->
                this.repository.findById(null)
        );
    }

    @Test
    void shouldFindByEmail() {
        final Id<User> id = Id.unique();
        final Email email = Email.load(EMAIL);
        final Password password = Password.encrypted(PASSWORD);
        final User user = User.load(id, USERNAME, email, password);
        this.repository.save(user);

        final Optional<User> anUserOpt = assertDoesNotThrow(() ->
                this.repository.findByEmail(EMAIL)
        );

        assertNotNull(anUserOpt);
        assertTrue(anUserOpt.isPresent());

        final User anUser = anUserOpt.get();
        assertEquals(id, anUser.getId());
        assertEquals(USERNAME, anUser.getUsername());
        assertEquals(email.getValue(), anUser.getEmail().getValue());
        assertEquals(password.getValue(), anUser.getPassword().getValue());
    }

    @Test
    void shouldNotFindByDifferentEmail() {
        final Id<User> id = Id.unique();
        final Email email = Email.load(EMAIL);
        final Password password = Password.encrypted(PASSWORD);
        final User user = User.load(id, USERNAME, email, password);
        this.repository.save(user);

        final String otherEmail = "luanpozzobon1@gmail.com";
        final Optional<User> anUserOpt = assertDoesNotThrow(() ->
                this.repository.findByEmail(otherEmail)
        );

        assertNotNull(anUserOpt);
        assertTrue(anUserOpt.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenLookingForNullEmail() {
        assertThrows(NullPointerException.class, () ->
                this.repository.findByEmail(null)
        );
    }

    @Test
    void shouldFindByUsername() {
        final Id<User> id = Id.unique();
        final Email email = Email.load(EMAIL);
        final Password password = Password.encrypted(PASSWORD);
        final User user = User.load(id, USERNAME, email, password);
        this.repository.save(user);

        final Optional<User> anUserOpt = assertDoesNotThrow(() ->
                this.repository.findByUsername(USERNAME)
        );

        assertNotNull(anUserOpt);
        assertTrue(anUserOpt.isPresent());

        final User anUser = anUserOpt.get();
        assertEquals(id, anUser.getId());
        assertEquals(USERNAME, anUser.getUsername());
        assertEquals(email.getValue(), anUser.getEmail().getValue());
        assertEquals(password.getValue(), anUser.getPassword().getValue());
    }

    @Test
    void shouldNotFindByDifferentUsername() {
        final Id<User> id = Id.unique();
        final Email email = Email.load(EMAIL);
        final Password password = Password.encrypted(PASSWORD);
        final User user = User.load(id, USERNAME, email, password);
        this.repository.save(user);

        final String otherUsername = "luanpozzobon1";
        final Optional<User> anUserOpt = assertDoesNotThrow(() ->
                this.repository.findByUsername(otherUsername)
        );

        assertNotNull(anUserOpt);
        assertTrue(anUserOpt.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenLookingForNullUsername() {
        assertThrows(NullPointerException.class, () ->
                this.repository.findByUsername(null)
        );
    }
}
