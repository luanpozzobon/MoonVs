package lpz.moonvs.infra.title.mapper;

import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Title;
import lpz.moonvs.infra.title.entity.TitleEntity;

import java.util.Optional;

public abstract class TitleMapper {
    public static TitleEntity from(final Title title) {
        if (title == null) return null;

        final TitleEntity entity = new TitleEntity();

        Optional.ofNullable(title.getId())
                .map(id -> Long.parseLong(id.getValue()))
                .ifPresent(entity::setId);

        entity.setTmdbId(title.getTmdbId());

        return entity;
    }

    public static Title to(final TitleEntity entity) {
        final Id<Title> id = Id.from(entity.getId());

        return Title.load(id, entity.getTmdbId());
    }
}
