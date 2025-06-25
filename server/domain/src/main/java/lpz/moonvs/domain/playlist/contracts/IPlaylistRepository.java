package lpz.moonvs.domain.playlist.contracts;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.contracts.model.PlaylistSearchQuery;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.seedwork.valueobject.Id;

import java.util.List;

public interface IPlaylistRepository {
    public Playlist save(final Playlist playlist);

    public List<Playlist> findAll(final Id<User> userId);

    public Playlist findById(final Id<Playlist> playlistId);

    public List<Playlist> findByTitle(final Id<User> userId, final String title);

    public List<Playlist> search(final Id<User> userId, final PlaylistSearchQuery query);

    public int update(final Playlist playlist);

    public int delete(final Playlist playlist);
}
