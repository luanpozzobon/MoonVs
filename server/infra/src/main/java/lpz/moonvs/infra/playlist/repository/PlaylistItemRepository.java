package lpz.moonvs.infra.playlist.repository;

import lpz.moonvs.domain.playlist.contracts.IPlaylistItemRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.entity.PlaylistItem;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Title;
import lpz.moonvs.infra.exception.DataAccessException;
import lpz.moonvs.infra.playlist.entity.PlaylistItemEntity;
import lpz.moonvs.infra.playlist.mapper.PlaylistItemMapper;
import lpz.utils.dao.postgresql.CRUDBuilderFactory;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlaylistItemRepository implements IPlaylistItemRepository {
    private final DataSource dataSource;

    public PlaylistItemRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public PlaylistItem save(final PlaylistItem playlistItem) {
        final PlaylistItemEntity entity = PlaylistItemMapper.from(playlistItem);
        try (final var connection = this.dataSource.getConnection()) {
            return PlaylistItemMapper.to(
                    new CRUDBuilderFactory(connection)
                            .insert(PlaylistItemEntity.class)
                            .returning()
                            .execute(entity)
                            .entities().get(0)
            );
        } catch (final SQLException e) {
            throw new DataAccessException("Error saving playlist item to database.", e);
        }
    }

    @Override
    public PlaylistItem findByPlaylistIdAndTitleId(final Id<Playlist> playlistId,
                                                   final Id<Title> titleId) {
        try (final var connection = this.dataSource.getConnection()) {
            final List<PlaylistItemEntity> entities =
                    new CRUDBuilderFactory(connection)
                            .select(PlaylistItemEntity.class)
                            .where("playlist_id").equal(UUID.fromString(playlistId.getValue()))
                            .where("title_id").equal(Long.parseLong(titleId.getValue()))
                            .execute().entities();

            return entities.stream()
                    .map(PlaylistItemMapper::to).findFirst()
                    .orElse(null);
        } catch (final SQLException e) {
            throw new DataAccessException("Error retrieving playlist item from database.", e);
        }
    }

    @Override
    public List<PlaylistItem> findAllByPlaylistId(final Id<Playlist> playlistId,
                                                  final Integer page) {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        query.append("playlist_id, ");
        query.append("title_id, ");
        query.append("type ");
        query.append("FROM ");
        query.append("mvs.playlist_item ");
        query.append("WHERE ");
        query.append("playlist_id = ?::uuid ");
        query.append("ORDER BY ");
        query.append("playlist_id ");
        query.append("LIMIT 50 ");
        query.append("OFFSET ?; ");

        try (final PreparedStatement stmt = this.dataSource.getConnection().prepareStatement(query.toString())) {
            stmt.setString(1, playlistId.getValue());
            stmt.setInt(2, (page - 1) * 50);

            final ResultSet rs = stmt.executeQuery();
            final List<PlaylistItem> items = new ArrayList<>();
            while (rs.next()) {
                final Id<Playlist> id = Id.from(rs.getString("playlist_id"));
                final Id<Title> titleId = Id.from(rs.getLong("title_id"));
                final String type = rs.getString("type");

                items.add(PlaylistItem.load(id, titleId, type));
            }

            return items;
        } catch (final SQLException e) {
            throw new DataAccessException("Error retrieving playlist item from database.", e);
        }
    }

    @Override
    public int getTotalPagesByPlaylistId(final Id<Playlist> playlistId) {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        query.append("COUNT(*) / 50.0 ");
        query.append("FROM ");
        query.append("mvs.playlist_item ");
        query.append("WHERE ");
        query.append("playlist_id = ?::uuid ");

        try (final PreparedStatement stmt = this.dataSource.getConnection().prepareStatement(query.toString())) {
            stmt.setString(1, playlistId.getValue());

            final ResultSet rs = stmt.executeQuery();
            rs.next();

            return (int) Math.ceil(rs.getDouble(1));
        } catch (final SQLException e) {
            throw new DataAccessException("Error retrieving playlist item from database.", e);
        }
    }

    @Override
    public int delete(final PlaylistItem playlistItem) {
        final PlaylistItemEntity entity = PlaylistItemMapper.from(playlistItem);
        try (final var connection = this.dataSource.getConnection()) {
            return new CRUDBuilderFactory(connection)
                    .delete(PlaylistItemEntity.class)
                    .execute(entity)
                    .lines();
        } catch (final SQLException e) {
            throw new DataAccessException("Error deleting playlist item from database.", e);
        }
    }
}
