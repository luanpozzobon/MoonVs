package luan.moonvs.models.responses;

import luan.moonvs.models.entities.User;

public record AccountResponseDTO(String username, String email, String message) {
    public AccountResponseDTO(User user) {
        this(user.getUsername(), user.getEmail(), "");
    }

    public AccountResponseDTO(String errorMessage) {
        this("", "", errorMessage);
    }

    public AccountResponseDTO(User user, String message) {
        this(user.getUsername(), user.getEmail(), message);
    }
}
