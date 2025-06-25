package lpz.moonvs.domain.auth.contracts;

public interface IPasswordEncryptor {
    String encrypt(final String raw);
    boolean matches(final String raw, final String encrypted);
}
