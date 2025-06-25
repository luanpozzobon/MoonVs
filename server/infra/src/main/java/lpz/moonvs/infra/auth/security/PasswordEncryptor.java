package lpz.moonvs.infra.auth.security;

import lpz.moonvs.domain.auth.contracts.IPasswordEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptor implements IPasswordEncryptor {
    private final PasswordEncoder encoder;

    public PasswordEncryptor(final PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String encrypt(final String raw) {
        return this.encoder.encode(raw);
    }

    @Override
    public boolean matches(final String raw, final String encrypted) {
        return this.encoder.matches(raw, encrypted);
    }
}
