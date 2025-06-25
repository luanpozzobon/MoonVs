package lpz.moonvs.infra.playlist.entity;

import lpz.utils.dao.annotations.Field;
import lpz.utils.dao.annotations.Table;

import java.util.UUID;

@Table(schema = "mvs", name = "playlist_item")
public class PlaylistItemEntity {
    @Field(updatable = false, primaryKey = true)
    private UUID playlistId;

    @Field(updatable = false, primaryKey = true)
    private Long titleId;

    @Field(updatable = false)
    private String type;

    public PlaylistItemEntity() { }

    public UUID getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(UUID playlistId) {
        this.playlistId = playlistId;
    }

    public Long getTitleId() {
        return titleId;
    }

    public void setTitleId(Long titleId) {
        this.titleId = titleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
