package lpz.moonvs.infra.playlist.repository;

import lpz.moonvs.domain.auth.contracts.IUserRepository;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.auth.valueobject.Email;
import lpz.moonvs.domain.auth.valueobject.Password;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.contracts.model.PlaylistSearchQuery;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.infra.exception.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
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
class PlaylistRepositoryIntegrationTest {
    private static final String TITLE = "Playlist";
    private static final String DESCRIPTION = "Description";

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
    private IPlaylistRepository repository;

    @Autowired
    private IUserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        final Email email = Email.load("luanpozzobon@gmail.com");
        final Password password = Password.encrypted("encrypted_password");

        this.user = this.userRepository.save(User.load(Id.unique(), "luanpozzobon", email, password));
    }

    @Test
    void shouldSaveSuccessfully() {
        final Id<Playlist> playlistId = Id.unique();
        final Playlist playlist = Playlist.load(playlistId, this.user.getId(), TITLE, DESCRIPTION);

        final Playlist savedPlaylist = assertDoesNotThrow(() ->
                this.repository.save(playlist)
        );

        assertNotNull(savedPlaylist);

        assertEquals(playlistId, savedPlaylist.getId());
        assertEquals(this.user.getId(), savedPlaylist.getUserId());
        assertEquals(TITLE, savedPlaylist.getTitle());
        assertEquals(DESCRIPTION, savedPlaylist.getDescription());
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
        final Id<Playlist> playlistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(playlistId, this.user.getId(), TITLE, DESCRIPTION);
        this.repository.save(aPlaylist);

        final String title = "Playlist1";
        final Playlist otherPlaylist = Playlist.load(playlistId, this.user.getId(), title, DESCRIPTION);

        final var exception = assertThrows(DataAccessException.class, () ->
                this.repository.save(otherPlaylist)
        );

        assertEquals("Error saving playlist to database.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSavingWithDuplicateTitle() {
        final Id<Playlist> aPlaylistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(aPlaylistId, this.user.getId(), TITLE, DESCRIPTION);
        this.repository.save(aPlaylist);

        final Id<Playlist> otherPlaylistId = Id.unique();
        final Playlist otherPlaylist = Playlist.load(otherPlaylistId, this.user.getId(), TITLE, DESCRIPTION);

        final var exception = assertThrows(DataAccessException.class, () ->
                this.repository.save(otherPlaylist)
        );

        assertEquals("Error saving playlist to database.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        final Playlist playlist = Playlist.load(null, this.user.getId(), TITLE, DESCRIPTION);

        assertThrows(NullPointerException.class, () ->
                this.repository.save(playlist)
        );
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {
        final Id<Playlist> playlistId = Id.unique();
        final Playlist playlist = Playlist.load(playlistId, null, TITLE, DESCRIPTION);

        assertThrows(NullPointerException.class, () ->
                this.repository.save(playlist)
        );
    }

    @Test
    void shouldThrowExceptionWhenUserIdDoesNotExist() {
        final Id<Playlist> playlistId = Id.unique();
        final Id<User> userId = Id.unique();
        final Playlist playlist = Playlist.load(playlistId, userId, TITLE, DESCRIPTION);

        final var exception = assertThrows(DataAccessException.class, () ->
                this.repository.save(playlist)
        );

        assertEquals("Error saving playlist to database.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTitleIsNull() {
        final Id<Playlist> playlistId = Id.unique();
        final Playlist playlist = Playlist.load(playlistId, this.user.getId(), null, DESCRIPTION);

        assertThrows(NullPointerException.class, () ->
                this.repository.save(playlist)
        );
    }

    @Test
    void shouldFindAllSuccessfully() {
        final Id<Playlist> aPlaylistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(aPlaylistId, this.user.getId(), TITLE, DESCRIPTION);
        this.repository.save(aPlaylist);

        final Id<Playlist> otherPlaylistId = Id.unique();
        final String title = "Playlist1";
        final Playlist otherPlaylist = Playlist.load(otherPlaylistId, this.user.getId(), title, DESCRIPTION);
        this.repository.save(otherPlaylist);

        final List<Playlist> playlists = assertDoesNotThrow(() ->
                this.repository.findAll(this.user.getId())
        );

        assertNotNull(playlists);

        assertFalse(playlists.isEmpty());
        assertEquals(2, playlists.size());
        assertEquals(this.user.getId(), playlists.getFirst().getUserId());
    }

    @Test
    void shouldReturnAnEmptyListWhenThereIsNoPlaylistForThatUser() {
        final Id<Playlist> aPlaylistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(aPlaylistId, this.user.getId(), TITLE, DESCRIPTION);
        this.repository.save(aPlaylist);

        final Id<Playlist> otherPlaylistId = Id.unique();
        final String title = "Playlist1";
        final Playlist otherPlaylist = Playlist.load(otherPlaylistId, this.user.getId(), title, DESCRIPTION);
        this.repository.save(otherPlaylist);

        final List<Playlist> playlists = assertDoesNotThrow(() ->
                this.repository.findAll(Id.unique())
        );

        assertNotNull(playlists);

        assertTrue(playlists.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenLookingForNullUserId() {
        assertThrows(NullPointerException.class, () ->
                this.repository.findAll(null)
        );
    }

    @Test
    void shouldFindById() {
        final Id<Playlist> playlistId = Id.unique();
        final Playlist playlist = Playlist.load(playlistId, this.user.getId(), TITLE, DESCRIPTION);
        this.repository.save(playlist);

        final Optional<Playlist> aPlaylistOpt = assertDoesNotThrow(() ->
                this.repository.findById(playlistId)
        );

        assertNotNull(aPlaylistOpt);
        assertTrue(aPlaylistOpt.isPresent());

        final Playlist aPlaylist = aPlaylistOpt.get();
        assertEquals(playlistId, aPlaylist.getId());
        assertEquals(this.user.getId(), aPlaylist.getUserId());
        assertEquals(TITLE, aPlaylist.getTitle());
        assertEquals(DESCRIPTION, aPlaylist.getDescription());
    }

    @Test
    void shouldNotFindByDifferentId(){
        final Id<Playlist> playlistId = Id.unique();
        final Playlist playlist = Playlist.load(playlistId, this.user.getId(), TITLE, DESCRIPTION);
        this.repository.save(playlist);

        final Optional<Playlist> aPlaylistOpt = assertDoesNotThrow(() ->
                this.repository.findById(Id.unique())
        );

        assertNotNull(aPlaylistOpt);
        assertTrue(aPlaylistOpt.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenLookingForNullId() {
        assertThrows(NullPointerException.class, () ->
                this.repository.findById(null)
        );
    }

    @Test
    void shouldFindByTitle() {
        final Id<Playlist> playlistId = Id.unique();
        final Playlist playlist = Playlist.load(playlistId, this.user.getId(), TITLE, DESCRIPTION);
        this.repository.save(playlist);

        final List<Playlist> playlists = assertDoesNotThrow(() ->
                this.repository.findByTitle(this.user.getId(), TITLE)
        );

        assertNotNull(playlists);
        assertFalse(playlists.isEmpty());

        final Playlist aPlaylist = playlists.getFirst();
        assertNotNull(aPlaylist);
        assertEquals(playlistId, aPlaylist.getId());
        assertEquals(this.user.getId(), aPlaylist.getUserId());
        assertEquals(TITLE, aPlaylist.getTitle());
        assertEquals(DESCRIPTION, aPlaylist.getDescription());
    }

    @Test
    void shouldNotFindByDifferentTitle() {
        final Id<Playlist> playlistId = Id.unique();
        final Playlist playlist = Playlist.load(playlistId, this.user.getId(), TITLE, DESCRIPTION);
        this.repository.save(playlist);

        final List<Playlist> playlists = assertDoesNotThrow(() ->
                this.repository.findByTitle(this.user.getId(), "Title1")
        );

        assertNotNull(playlists);
        assertTrue(playlists.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenLookingForNullTitle() {
        assertThrows(NullPointerException.class, () ->
                this.repository.findByTitle(this.user.getId(), null)
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"Playlist", "playlist", "PLAYLIST", "play", "list", "PlayList", "lay", "LIS"})
    void shouldSearchSuccessfully(final String title) {
        final Id<Playlist> playlistId = Id.unique();
        final Playlist playlist = Playlist.load(playlistId, this.user.getId(), TITLE, DESCRIPTION);
        this.repository.save(playlist);

        final PlaylistSearchQuery query = new PlaylistSearchQuery(title);

        final List<Playlist> playlists = assertDoesNotThrow(() ->
                this.repository.search(this.user.getId(), query)
        );

        assertNotNull(playlists);
        assertFalse(playlists.isEmpty());

        playlists.forEach(p -> {
            assertNotNull(p);
            assertEquals(this.user.getId(), p.getUserId());
            if (title != null)
                assertTrue(p.getTitle().toLowerCase().contains(title.toLowerCase()));
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"Watch", "watch"})
    void shouldNotFindSearchingForInexistentTitles(final String inexistentTitle) {
        final Id<Playlist> playlistId = Id.unique();
        final Playlist playlist = Playlist.load(playlistId, this.user.getId(), TITLE, DESCRIPTION);
        this.repository.save(playlist);

        final PlaylistSearchQuery query = new PlaylistSearchQuery(inexistentTitle);

        final List<Playlist> playlists = assertDoesNotThrow(() ->
                this.repository.search(this.user.getId(), query)
        );

        assertNotNull(playlists);
        assertTrue(playlists.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenSearchingForNullUserId() {
        final PlaylistSearchQuery query = new PlaylistSearchQuery(TITLE);

        assertThrows(NullPointerException.class, () ->
                this.repository.search(null, query)
        );
    }

    @Test
    void shouldUpdateSuccessfully() {
        final Id<Playlist> playlistId = Id.unique();
        final Playlist playlist = Playlist.load(playlistId, this.user.getId(), TITLE, DESCRIPTION);
        this.repository.save(playlist);

        final String title = "New Title";
        final String description = "New Description";
        playlist.rename(null, title);
        playlist.updateDescription(null, description);

        final int lines = assertDoesNotThrow(() -> this.repository.update(playlist));
        assertEquals(1, lines);

        final Playlist retrievedPlaylist = this.repository.findById(playlistId).get();

        assertNotNull(retrievedPlaylist);
        assertEquals(playlistId, retrievedPlaylist.getId());
        assertEquals(this.user.getId(), retrievedPlaylist.getUserId());
        assertEquals(title, retrievedPlaylist.getTitle());
        assertEquals(description, retrievedPlaylist.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNullEntity() {
        assertThrows(NullPointerException.class, () ->
                this.repository.update(null)
        );
    }

    @Test
    void shouldDeleteSuccessfully() {
        final Id<Playlist> playlistId = Id.unique();
        final Playlist playlist = Playlist.load(playlistId, this.user.getId(), TITLE, DESCRIPTION);
        this.repository.save(playlist);

        final int lines = assertDoesNotThrow(() -> this.repository.delete(playlist));
        assertEquals(1, lines);

        final Optional<Playlist> retrievedPlaylist = this.repository.findById(playlistId);

        assertNotNull(retrievedPlaylist);
        assertTrue(retrievedPlaylist.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNull() {
        assertThrows(NullPointerException.class, () -> this.repository.delete(null));
    }
}
