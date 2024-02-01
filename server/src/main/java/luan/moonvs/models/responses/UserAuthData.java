package luan.moonvs.models.responses;

import java.util.UUID;

public record UserAuthData(UUID idUser, String token) {
    public UserAuthData() {
        this(null, "");
    }
}
