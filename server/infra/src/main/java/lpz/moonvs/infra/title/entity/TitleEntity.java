package lpz.moonvs.infra.title.entity;

import lpz.utils.dao.annotations.Field;
import lpz.utils.dao.annotations.Table;

@Table(schema = "mvs", name = "title")
public class TitleEntity {
    @Field(updatable = false, primaryKey = true)
    private Long id;

    @Field(updatable = false, nullable = false)
    private Long tmdbId;

    public TitleEntity() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(Long tmdbId) {
        this.tmdbId = tmdbId;
    }
}
