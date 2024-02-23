package luan.moonvs.models.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class MovieListContentId implements Serializable {
    private Long idList;
    private Integer idContent;
}
