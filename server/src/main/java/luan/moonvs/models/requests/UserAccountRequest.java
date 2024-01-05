package luan.moonvs.models.requests;

import java.util.UUID;

public record UserAccountRequest(UUID idUser,
                                 String username,
                                 String email,
                                 String password) { }
