package luan.moonvs.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovieListContentId implements Serializable {
    private Long idList;
    private Integer idContent;
}
