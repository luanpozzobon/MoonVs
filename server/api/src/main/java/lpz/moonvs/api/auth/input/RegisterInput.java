package lpz.moonvs.api.auth.input;

public record RegisterInput(String email,
                            String username,
                            String password) { }
