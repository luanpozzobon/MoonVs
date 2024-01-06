package luan.moonvs.models.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.UUID;

@JsonIgnoreProperties
public record UserAccountRequest(UUID idUser,
                                 String username,
                                 String email,
                                 LocalDate birthDate,
                                 String password) { }
