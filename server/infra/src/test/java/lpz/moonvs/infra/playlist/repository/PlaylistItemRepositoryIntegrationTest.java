package lpz.moonvs.infra.playlist.repository;

import lpz.moonvs.domain.auth.contracts.IUserRepository;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.auth.valueobject.Email;
import lpz.moonvs.domain.auth.valueobject.Password;
import lpz.moonvs.domain.playlist.contracts.IPlaylistItemRepository;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.entity.PlaylistItem;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Title;
import lpz.moonvs.infra.exception.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PlaylistItemRepositoryIntegrationTest {
    private static final Long TITLE_ID = 1L;
    private static final String TYPE = "TV";

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
    private IPlaylistItemRepository repository;

    @Autowired
    private IPlaylistRepository playlistRepository;

    @Autowired
    private IUserRepository userRepository;

    private Playlist playlist;

    @BeforeEach
    void setUp() {
        final Email email = Email.load("luanpozzobon@gmail.com");
        final Password password = Password.encrypted("encrypted_password");
        final User user = User.load(Id.unique(), "luanpozzobon", email, password);

        this.userRepository.save(user);

        this.playlist = Playlist.load(Id.unique(), user.getId(), "Playlist", "Description");

        this.playlistRepository.save(playlist);
    }

    @Test
    void shouldSaveSuccessfully() {
        final Id<Title> titleId = Id.from(TITLE_ID);
        final PlaylistItem playlistItem = PlaylistItem.load(this.playlist.getId(), titleId, TYPE);

        final PlaylistItem savedPlaylistItem = assertDoesNotThrow(() ->
                this.repository.save(playlistItem)
        );

        assertNotNull(savedPlaylistItem);
        assertEquals(this.playlist.getId(), savedPlaylistItem.getPlaylistId());
        assertEquals(titleId, savedPlaylistItem.getTitleId());
        assertEquals(TYPE, savedPlaylistItem.getType());
    }

    @Test
    void shouldThrowExceptionWhenSavingNullEntity() {
        assertThrows(NullPointerException.class, () -> this.repository.save(null));
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        final Id<Title> titleId = Id.from(TITLE_ID);
        final PlaylistItem playlistItem = PlaylistItem.load(null, titleId, TYPE);

        assertThrows(NullPointerException.class, () ->
                this.repository.save(playlistItem)
        );
    }

    @Test
    void shouldThrowExceptionWhenTitleIdIsNull() {
        final PlaylistItem playlistItem = PlaylistItem.load(this.playlist.getId(), null, TYPE);

        assertThrows(NullPointerException.class, () ->
            this.repository.save(playlistItem)
        );
    }

    @Test
    void shouldThrowExceptionWhenSavingSuplicated() {
        final Id<Title> titleId = Id.from(TITLE_ID);
        final PlaylistItem playlistItem = PlaylistItem.load(this.playlist.getId(), titleId, TYPE);
        this.repository.save(playlistItem);

        final var exception = assertThrows(DataAccessException.class, () ->
                this.repository.save(playlistItem)
        );

        assertEquals("Error saving playlist item to database.", exception.getMessage());
    }

    @Test
    void shouldFindByPlaylistIdAndTitleId() {
        final Id<Title> titleId = Id.from(TITLE_ID);
        final PlaylistItem playlistItem = PlaylistItem.load(this.playlist.getId(), titleId, TYPE);
        this.repository.save(playlistItem);

        final Optional<PlaylistItem> aPlaylistItemOpt = assertDoesNotThrow(() ->
                this.repository.findByPlaylistIdAndTitleId(this.playlist.getId(), titleId)
        );

        assertNotNull(aPlaylistItemOpt);
        assertTrue(aPlaylistItemOpt.isPresent());

        final PlaylistItem aPlaylistItem = aPlaylistItemOpt.get();
        assertNotNull(aPlaylistItem);
        assertEquals(this.playlist.getId(), aPlaylistItem.getPlaylistId());
        assertEquals(titleId, aPlaylistItem.getTitleId());
        assertEquals(TYPE, aPlaylistItem.getType());
    }

    @Test
    void shouldNotFindByDifferentId() {
        final Id<Title> titleId = Id.from(TITLE_ID);
        final PlaylistItem playlistItem = PlaylistItem.load(this.playlist.getId(), titleId, TYPE);
        this.repository.save(playlistItem);

        final Optional<PlaylistItem> aPlaylistItemOpt = assertDoesNotThrow(() ->
                this.repository.findByPlaylistIdAndTitleId(Id.unique(), titleId)
        );

        assertNotNull(aPlaylistItemOpt);
        assertTrue(aPlaylistItemOpt.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenLookingForNullPlaylistId() {
        final Id<Title> titleId = Id.unique();
        assertThrows(NullPointerException.class, () ->
                this.repository.findByPlaylistIdAndTitleId(null, titleId)
        );
    }

    @Test
    void shouldThrowExceptionWhenLookingForNullTitleId() {
        final Id<Playlist> playlistId = Id.unique();
        assertThrows(NullPointerException.class, () ->
                this.repository.findByPlaylistIdAndTitleId(playlistId, null)
        );
    }

    @Test
    void shouldFindAllByPlaylistId() {
        final Id<Title> titleId = Id.from(TITLE_ID);
        final PlaylistItem playlistItem = PlaylistItem.load(this.playlist.getId(), titleId, TYPE);
        this.repository.save(playlistItem);

        final List<PlaylistItem> playlistItems = assertDoesNotThrow(() ->
                this.repository.findAllByPlaylistId(this.playlist.getId(), 1)
        );

        assertNotNull(playlistItems);
        assertFalse(playlistItems.isEmpty());

        playlistItems.forEach(pi -> {
            assertEquals(this.playlist.getId(), pi.getPlaylistId());
        });
    }

    @Test
    void shouldThrowExceptionWhenFindingAllByNullPlaylistId() {
        assertThrows(NullPointerException.class, () ->
                this.repository.findAllByPlaylistId(null, 1)
        );
    }

    @Test
    void shouldThrowExceptionWhenFindingByIdWithNullPage() {
        final Id<Playlist> playlistId = this.playlist.getId();
        assertThrows(NullPointerException.class, () ->
                this.repository.findAllByPlaylistId(playlistId, null)
        );
    }

    @Test
    void shouldGetTotalPagesByPlaylistId() {
        final Id<Title> titleId = Id.from(TITLE_ID);
        final PlaylistItem playlistItem = PlaylistItem.load(this.playlist.getId(), titleId, TYPE);
        this.repository.save(playlistItem);

        final int pages = assertDoesNotThrow(() ->
                this.repository.getTotalPagesByPlaylistId(this.playlist.getId())
        );

        assertEquals(1, pages);
    }

    @Test
    void shouldThrowExceptionWhenGettingTotalPagesByNullPlaylistId() {
        assertThrows(NullPointerException.class, () ->
            this.repository.getTotalPagesByPlaylistId(null)
        );
    }

    @Test
    void shouldDeleteSuccessfully() {
        final Id<Title> titleId = Id.from(TITLE_ID);
        final PlaylistItem playlistItem = PlaylistItem.load(this.playlist.getId(), titleId, TYPE);
        this.repository.save(playlistItem);

        final int lines = assertDoesNotThrow(() ->
                this.repository.delete(playlistItem)
        );

        assertEquals(1, lines);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNullEntity() {
        assertThrows(NullPointerException.class, () ->
                this.repository.delete(null)
        );
    }
}
