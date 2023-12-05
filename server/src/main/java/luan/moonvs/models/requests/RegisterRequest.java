package luan.moonvs.models.requests;

import java.time.LocalDate;

public record RegisterRequest(String username,
                              String email,
                              String password,
                              String confirmedPassword,
                              LocalDate birthDate) { }
