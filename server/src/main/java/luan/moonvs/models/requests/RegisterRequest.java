package luan.moonvs.models.requests;

import java.time.LocalDate;

public record RegisterRequest(String username,
                              String email,
                              LocalDate birthDate,
                              String password) { }
