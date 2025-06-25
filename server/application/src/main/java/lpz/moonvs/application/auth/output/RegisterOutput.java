package lpz.moonvs.application.auth.output;

import lpz.moonvs.domain.auth.entity.User;

public record RegisterOutput(String id,
                             String email,
                             String username) {
    public static RegisterOutput from(User user) {
        return new RegisterOutput(user.getId().getValue(), user.getEmail().getValue(), user.getUsername());
    }
}
