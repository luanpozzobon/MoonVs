package luan.moonvs.models.responses;

import luan.moonvs.models.entities.User;
import org.springframework.http.HttpStatus;

public record UserAccountResponse(HttpStatus status,
                                  String username,
                                  String email,
                                  String message) {

    public UserAccountResponse(HttpStatus status, String message) {
        this(status, "", "", message);
    }

    public UserAccountResponse(HttpStatus status, User user, String message) {
        this(status, user.getUsername(), user.getEmail(), message);
    }
}
