package lpz.moonvs.infra.playlist.entity;

import lpz.utils.dao.annotations.Field;
import lpz.utils.dao.annotations.Table;

import java.util.UUID;

@Table(schema = "mvs", name = "playlist")
public class PlaylistEntity {
    @Field(updatable = false, primaryKey = true)
    private UUID id;

    @Field(updatable = false, nullable = false)
    private UUID userId;

    @Field(nullable = false, length = 64)
    private String title;

    @Field(length = 255)
    private String description;

    public PlaylistEntity() { }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
