package lpz.moonvs.infra.title.repository;

import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.contracts.ITitleRepository;
import lpz.moonvs.domain.title.entity.Title;
import lpz.moonvs.infra.exception.DataAccessException;
import lpz.moonvs.infra.title.entity.TitleEntity;
import lpz.moonvs.infra.title.mapper.TitleMapper;
import lpz.utils.dao.postgresql.CRUDBuilderFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class TitleRepository implements ITitleRepository {
    private final DataSource dataSource;

    public TitleRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Title save(final Title title) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Title findById(final Id<Title> id) {
        try (final var connection = this.dataSource.getConnection()) {
            final List<TitleEntity> entities =
                    new CRUDBuilderFactory(connection)
                            .select(TitleEntity.class)
                            .where("id").equal(Long.parseLong(id.getValue()))
                            .execute()
                            .entities();

            return entities.stream()
                    .map(TitleMapper::to).findFirst()
                    .orElse(null);
        } catch (final SQLException e) {
            throw new DataAccessException("Error retrieving title from database.", e);
        }
    }

    @Override
    public Title findByTmdbId(final Long tmdbId) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
