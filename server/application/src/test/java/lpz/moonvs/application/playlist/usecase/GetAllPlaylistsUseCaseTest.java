package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.GetAllPlaylistsCommand;
import lpz.moonvs.application.playlist.output.GetAllPlaylistsOutput;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllPlaylistsUseCaseTest {

    @Mock
    private IPlaylistRepository repository;

    @InjectMocks
    private GetAllPlaylistsUseCase useCase;

    @Test
    void shouldExecuteSuccessfully() {
        when(this.repository.findAll(any(Id.class))).thenReturn(List.of());

        final GetAllPlaylistsOutput output = assertDoesNotThrow(() ->
                this.useCase.execute(new GetAllPlaylistsCommand(Id.unique()))
        );

        assertNotNull(output);
    }
}
