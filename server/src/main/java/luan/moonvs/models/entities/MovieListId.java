package luan.moonvs.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor

@Embeddable
public class MovieListId implements Serializable {
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_list")
    private int idList;

    @Column(name = "id_user")
    private UUID idUser;
}
