package luan.moonvs.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Embeddable
public class ContentAndUserId implements Serializable {
    @Column(name = "id_user")
    private UUID idUser;

    @Column(name = "id_content")
    private int idContent;

    public ContentAndUserId(UUID idUser) {
        this.idUser = idUser;
    }

}
