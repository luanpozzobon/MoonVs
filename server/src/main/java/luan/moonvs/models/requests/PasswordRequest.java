package luan.moonvs.models.requests;

public record PasswordRequest(String password,
                              String confirmedPassword) { }
