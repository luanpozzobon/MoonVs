package lpz.moonvs.application.auth.output;

import lpz.moonvs.domain.auth.entity.User;

public record LoginOutput(String id,
                          String email,
                          String username) {
    public static LoginOutput from(User user) {
        return new LoginOutput(user.getId().getValue(), user.getEmail().getValue(), user.getUsername());
    }
}
