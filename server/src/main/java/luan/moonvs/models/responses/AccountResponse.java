package luan.moonvs.models.responses;

import luan.moonvs.models.entities.User;

public record AccountResponse(String username,
                              String email,
                              String message) {
    public AccountResponse(User user) {
        this(user.getUsername(), user.getEmail(), "");
    }

    public AccountResponse(String errorMessage) {
        this("", "", errorMessage);
    }

    public AccountResponse(User user, String message) {
        this(user.getUsername(), user.getEmail(), message);
    }

    public AccountResponse(UserAccountResponse userAccountResponse) {
        this(userAccountResponse.username(), userAccountResponse.email(), userAccountResponse.message());
    }
}
