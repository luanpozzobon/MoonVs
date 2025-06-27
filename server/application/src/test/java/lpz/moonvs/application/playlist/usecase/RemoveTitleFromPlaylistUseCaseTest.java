package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.RemoveTitleFromPlaylistCommand;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.contracts.IPlaylistItemRepository;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.entity.PlaylistItem;
import lpz.moonvs.domain.playlist.exception.PlaylistItemNotFoundException;
import lpz.moonvs.domain.playlist.exception.PlaylistNotFoundException;
import lpz.moonvs.domain.seedwork.exception.NoAccessToResourceException;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Title;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemoveTitleFromPlaylistUseCaseTest {

    @Mock
    private IPlaylistRepository playlistRepository;

    @Mock
    private IPlaylistItemRepository repository;

    @InjectMocks
    private RemoveTitleFromPlaylistUseCase useCase;

    @Test
    void shouldExecuteSuccessfully() {
        final Id<User> userId = Id.unique();
        final Id<Playlist> playlistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(playlistId, userId, "Playlist", "Description");

        final Id<Title> titleId = Id.unique();
        final PlaylistItem aPlaylistItem = PlaylistItem.load(playlistId, titleId, "TV");

        when(this.playlistRepository.findById(any(Id.class))).thenReturn(Optional.of(aPlaylist));
        when(this.repository.findByPlaylistIdAndTitleId(any(Id.class), any(Id.class))).thenReturn(Optional.of(aPlaylistItem));

        assertDoesNotThrow(() -> this.useCase.execute(new RemoveTitleFromPlaylistCommand(userId, playlistId, titleId)));
    }

    @Test
    void shouldThrowPlaylistNotFoundException() {
        when(this.playlistRepository.findById(any(Id.class))).thenReturn(Optional.empty());

        final var command = new RemoveTitleFromPlaylistCommand(Id.unique(), Id.unique(), Id.unique());
        final var exception = assertThrows(PlaylistNotFoundException.class, () ->
                this.useCase.execute(command)
        );

        assertEquals("There is no playlist with the given id.", exception.getMessage());
    }

    @Test
    void shouldThrowNoAccessToResourceWhenUserIdIsDifferent() {
        final Id<User> userId = Id.unique();
        final Id<Playlist> playlistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(playlistId, userId, "Playlist", "Description");

        when(this.playlistRepository.findById(any(Id.class))).thenReturn(Optional.of(aPlaylist));

        final var command = new RemoveTitleFromPlaylistCommand(Id.unique(), playlistId, Id.unique());
        final var exception = assertThrows(NoAccessToResourceException.class, () ->
                this.useCase.execute(command)
        );

        assertEquals("The authenticated user doesn't have access to this playlist.", exception.getMessage());
    }

    @Test
    void shouldThrowPlaylistItemNotFoundException() {
        final Id<User> userId = Id.unique();
        final Id<Playlist> playlistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(playlistId, userId, "Playlist", "Description");

        when(this.playlistRepository.findById(any(Id.class))).thenReturn(Optional.of(aPlaylist));

        final var command = new RemoveTitleFromPlaylistCommand(userId, playlistId, Id.unique());
        final var exception = assertThrows(PlaylistItemNotFoundException.class, () ->
                this.useCase.execute(command)
        );

        assertEquals("This title is not in the playlist.", exception.getMessage());
    }
}
