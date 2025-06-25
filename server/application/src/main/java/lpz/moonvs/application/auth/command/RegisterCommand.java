package lpz.moonvs.application.auth.command;

public record RegisterCommand(String email,
                              String username,
                              String password) {
}
