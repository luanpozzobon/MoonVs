package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.UpdatePlaylistCommand;
import lpz.moonvs.application.playlist.output.UpdatePlaylistOutput;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.exception.PlaylistAlreadyExistsException;
import lpz.moonvs.domain.playlist.exception.PlaylistNotFoundException;
import lpz.moonvs.domain.seedwork.exception.NoAccessToResourceException;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdatePlaylistUseCaseTest {
    private static final String VALID_TITLE = "Playlist";
    private static final String VALID_DESCRIPTION = "Description";

    @Mock
    private IPlaylistRepository repository;

    @InjectMocks
    private UpdatePlaylistUseCase useCase;

    @Test
    void shouldExecuteSuccessfully() {
        final Id<User> userId = Id.unique();
        final Id<Playlist> playlistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(playlistId, userId, VALID_TITLE, VALID_DESCRIPTION);

        when(this.repository.findById(any(Id.class))).thenReturn(Optional.of(aPlaylist));
        when(this.repository.findByTitle(any(Id.class), anyString())).thenReturn(Collections.EMPTY_LIST);

        final UpdatePlaylistOutput output = assertDoesNotThrow(() ->
                this.useCase.execute(new UpdatePlaylistCommand(userId, playlistId, "New Title", "New Description"))
        );

        assertNotNull(output);
        assertEquals(playlistId.getValue(), output.id());
        assertEquals("New Title", output.title());
        assertEquals("New Description", output.description());
    }

    @Test
    void shouldThrowPlaylistNotFoundException() {
        when(this.repository.findById(any(Id.class))).thenReturn(Optional.empty());

        final var command = new UpdatePlaylistCommand(Id.unique(), Id.unique(), VALID_TITLE, VALID_DESCRIPTION);
        final var exception = assertThrows(PlaylistNotFoundException.class, () ->
                this.useCase.execute(command)
        );

        assertEquals("There is no playlist with the given id.", exception.getMessage());
    }

    @Test
    void shouldThrowNoAccessToResourceExceptionWhenDifferentUserId() {
        final Id<User> userId = Id.unique();
        final Id<Playlist> playlistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(playlistId, userId, VALID_TITLE, VALID_DESCRIPTION);

        when(this.repository.findById(any(Id.class))).thenReturn(Optional.of(aPlaylist));

        final var command = new UpdatePlaylistCommand(Id.unique(), playlistId, VALID_TITLE, VALID_DESCRIPTION);
        final var exception = assertThrows(NoAccessToResourceException.class, () ->
                this.useCase.execute(command)
        );

        assertEquals("The authenticated user doesn't have access to this playlist.", exception.getMessage());
    }

    @Test
    void shouldNotRenameWhenTitleIsNull() {
        final Id<User> userId = Id.unique();
        final Id<Playlist> playlistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(playlistId, userId, VALID_TITLE, VALID_DESCRIPTION);

        when(this.repository.findById(any(Id.class))).thenReturn(Optional.of(aPlaylist));

        final UpdatePlaylistOutput output = assertDoesNotThrow(() ->
                this.useCase.execute(new UpdatePlaylistCommand(userId, playlistId, null, VALID_DESCRIPTION))
        );

        assertNotNull(output);
        assertEquals(playlistId.getValue(), output.id());
        assertEquals(VALID_TITLE, output.title());
    }

    @Test
    void shouldNotRenameWhenSameTitle() {
        final Id<User> userId = Id.unique();
        final Id<Playlist> playlistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(playlistId, userId, VALID_TITLE, VALID_DESCRIPTION);

        when(this.repository.findById(any(Id.class))).thenReturn(Optional.of(aPlaylist));

        final UpdatePlaylistOutput output = assertDoesNotThrow(() ->
                this.useCase.execute(new UpdatePlaylistCommand(userId, playlistId, VALID_TITLE, VALID_DESCRIPTION))
        );

        assertNotNull(output);
        assertEquals(playlistId.getValue(), output.id());
        assertEquals(VALID_TITLE, output.title());
    }

    @Test
    void shouldThrowPlaylistAlreadyExistsExceptionWhenTitleExists() {
        final Id<User> userId = Id.unique();
        final Id<Playlist> playlistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(playlistId, userId, VALID_TITLE, VALID_DESCRIPTION);
        final Playlist otherPlaylist = Playlist.load(Id.unique(), userId, "New Title", VALID_DESCRIPTION);

        when(this.repository.findById(any(Id.class))).thenReturn(Optional.of(aPlaylist));
        when(this.repository.findByTitle(any(Id.class), anyString())).thenReturn(List.of(otherPlaylist));

        final var command = new UpdatePlaylistCommand(userId, playlistId, "New Title", VALID_DESCRIPTION);
        final var exception = assertThrows(PlaylistAlreadyExistsException.class, () ->
                this.useCase.execute(command)
        );

        assertEquals("There is already a playlist created with this title.", exception.getMessage());
        assertEquals(1, exception.getErrors().size());
        assertEquals("title", exception.getErrors().getFirst().getKey());
        assertEquals("New Title", exception.getErrors().getFirst().getMessage());
    }

    @Test
    void shouldNotUpdateDescriptionWhenDescriptionIsNull() {
        final Id<User> userId = Id.unique();
        final Id<Playlist> playlistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(playlistId, userId, VALID_TITLE, VALID_DESCRIPTION);

        when(this.repository.findById(any(Id.class))).thenReturn(Optional.of(aPlaylist));

        final UpdatePlaylistOutput output = assertDoesNotThrow(() ->
                this.useCase.execute(new UpdatePlaylistCommand(userId, playlistId, VALID_TITLE, null))
        );

        assertNotNull(output);
        assertEquals(playlistId.getValue(), output.id());
        assertEquals(VALID_DESCRIPTION, output.description());
    }

    @Test
    void shouldNotUpdateDescriptionWhenSameDescription() {
        final Id<User> userId = Id.unique();
        final Id<Playlist> playlistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(playlistId, userId, VALID_TITLE, VALID_DESCRIPTION);

        when(this.repository.findById(any(Id.class))).thenReturn(Optional.of(aPlaylist));

        final UpdatePlaylistOutput output = assertDoesNotThrow(() ->
                this.useCase.execute(new UpdatePlaylistCommand(userId, playlistId, VALID_TITLE, VALID_DESCRIPTION))
        );

        assertNotNull(output);
        assertEquals(playlistId.getValue(), output.id());
        assertEquals(VALID_DESCRIPTION, output.description());
    }
}
