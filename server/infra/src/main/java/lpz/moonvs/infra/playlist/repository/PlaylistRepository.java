package lpz.moonvs.infra.playlist.repository;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.contracts.model.PlaylistSearchQuery;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.infra.exception.DataAccessException;
import lpz.moonvs.infra.playlist.entity.PlaylistEntity;
import lpz.moonvs.infra.playlist.mapper.PlaylistMapper;
import lpz.utils.dao.SelectBuilder;
import lpz.utils.dao.postgresql.CRUDBuilderFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PlaylistRepository implements IPlaylistRepository {
    private final DataSource dataSource;

    public PlaylistRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Playlist save(final Playlist playlist) {
        final PlaylistEntity entity = PlaylistMapper.from(playlist);

        try (var connection = this.dataSource.getConnection()) {
            return PlaylistMapper.to(
                    new CRUDBuilderFactory(connection)
                            .insert(PlaylistEntity.class)
                            .returning()
                            .execute(entity)
                            .entities().get(0)
            );
        } catch (SQLException e) {
            throw new DataAccessException("Error saving playlist to database.", e);
        }
    }

    @Override
    public List<Playlist> findAll(Id<User> userId) {
        try (var connection = this.dataSource.getConnection()) {
            final List<PlaylistEntity> entities =
                    new CRUDBuilderFactory(connection)
                            .select(PlaylistEntity.class)
                            .where("user_id").equal(UUID.fromString(userId.getValue()))
                            .execute().entities();

            return entities.stream().map(PlaylistMapper::to).toList();
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving playlists from database.", e);
        }
    }

    @Override
    public Optional<Playlist> findById(Id<Playlist> playlistId) {
        try (var connection = this.dataSource.getConnection()) {
            final List<PlaylistEntity> entities =
                    new CRUDBuilderFactory(connection)
                            .select(PlaylistEntity.class)
                            .where("id").equal(UUID.fromString(playlistId.getValue()))
                            .execute().entities();

            return entities.stream().findFirst()
                    .map(PlaylistMapper::to);
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving playlists from database.", e);
        }
    }

    @Override
    public List<Playlist> findByTitle(Id<User> userId, String title) {
        try (var connection = this.dataSource.getConnection()) {
            final List<PlaylistEntity> entities =
                    new CRUDBuilderFactory(connection)
                            .select(PlaylistEntity.class)
                            .where("user_id").equal(UUID.fromString(userId.getValue()))
                            .where("title").equal(title)
                            .execute().entities();

            return entities.stream().map(PlaylistMapper::to).toList();
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving playlists from database.", e);
        }
    }

    @Override
    public List<Playlist> search(Id<User> userId, PlaylistSearchQuery query) {
        try (var connection = this.dataSource.getConnection()) {
            SelectBuilder<PlaylistEntity> select =
                    new CRUDBuilderFactory(connection)
                            .select(PlaylistEntity.class)
                            .where("user_id").equal(UUID.fromString(userId.getValue()));

            if (query.title() != null && !query.title().isBlank())
                select = select.where("title").ilike(query.title());

            return select.execute()
                    .entities().stream()
                    .map(PlaylistMapper::to)
                    .toList();
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving playlists from database.", e);
        }
    }

    @Override
    public int update(Playlist playlist) {
        try (var connection = this.dataSource.getConnection()) {
            final PlaylistEntity entity = PlaylistMapper.from(playlist);

            return new CRUDBuilderFactory(connection)
                    .update(PlaylistEntity.class)
                    .execute(entity)
                    .lines();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating playlist in database.", e);
        }
    }

    @Override
    public int delete(Playlist playlist) {
        try (var connection = this.dataSource.getConnection()) {
            final PlaylistEntity entity = PlaylistMapper.from(playlist);

            return new CRUDBuilderFactory(connection)
                    .delete(PlaylistEntity.class)
                    .execute(entity)
                    .lines();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting playlist in database.", e);
        }
    }
}
