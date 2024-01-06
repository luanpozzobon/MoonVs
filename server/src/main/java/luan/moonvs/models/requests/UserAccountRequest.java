package luan.moonvs.models.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserAccountRequest(UUID idUser,
                                 String username,
                                 String email,
                                 LocalDate birthDate,
                                 String password) { }
