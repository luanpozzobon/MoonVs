package luan.moonvs.models.requests;

import java.time.LocalDate;

@Deprecated
public record RegisterRequest(String username,
                              String email,
                              LocalDate birthDate,
                              String password) { }
