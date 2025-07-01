package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.CreatePlaylistCommand;
import lpz.moonvs.application.playlist.output.CreatePlaylistOutput;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.exception.PlaylistAlreadyExistsException;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePlaylistUseCaseTest {
    private static final String VALID_TITLE = "Playlist";
    private static final String VALID_DESCRIPTION = "Description";

    @Mock
    private IPlaylistRepository repository;

    @InjectMocks
    private CreatePlaylistUseCase useCase;

    @Test
    void shouldExecuteSuccessfully() {
        final Playlist aPlaylist = Playlist.load(Id.unique(), Id.unique(), VALID_TITLE, VALID_DESCRIPTION);

        when(this.repository.findByTitle(any(Id.class), anyString())).thenReturn(Collections.EMPTY_LIST);
        when(this.repository.save(any(Playlist.class))).thenReturn(aPlaylist);

        final CreatePlaylistOutput output = assertDoesNotThrow(() ->
                this.useCase.execute(new CreatePlaylistCommand(Id.unique(), VALID_TITLE, VALID_DESCRIPTION))
        );

        assertNotNull(output);
        assertNotNull(output.id());
        assertEquals(VALID_TITLE, output.title());
        assertEquals(VALID_DESCRIPTION, output.description());
    }

    @Test
    void shouldThrowPlaylistDoesNotExistsExceptionWhenTitleExists() {
        final Playlist aPlaylist = Playlist.load(Id.unique(), Id.unique(), VALID_TITLE, VALID_DESCRIPTION);

        when(this.repository.findByTitle(any(Id.class), anyString())).thenReturn(List.of(aPlaylist));

        final var command = new CreatePlaylistCommand(Id.unique(), VALID_TITLE, VALID_DESCRIPTION);
        final var exception = assertThrows(PlaylistAlreadyExistsException.class, () ->
                this.useCase.execute(command)
        );

        assertEquals(PlaylistAlreadyExistsException.ERROR_KEY, exception.getMessage());
        assertEquals(1, exception.getErrors().size());
        assertEquals("title", exception.getErrors().getFirst().getKey());
        assertEquals(CreatePlaylistUseCase.ALREADY_EXISTS_ERROR_KEY, exception.getErrors().getFirst().getMessage());
    }
}
