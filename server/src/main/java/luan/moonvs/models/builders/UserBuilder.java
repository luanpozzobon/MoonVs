package luan.moonvs.models.builders;

import luan.moonvs.models.entities.User;
import luan.moonvs.models.requests.RegisterRequest;
import luan.moonvs.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

@Component
public class UserBuilder {
    private User user;

    private final UserRepository repository;

    @Autowired
    public UserBuilder(UserRepository repository) {
        this.repository = repository;
        this.user = new User();
    }


    public UserBuilder fromAuthUser(User authUser) {
        user = authUser.clone();
        return this;
    }

    public UserBuilder withUsername(String username) throws IllegalArgumentException {
        if (username == null || username.isBlank())
            return this;

        isUsernameValid(username);
        this.user.setUsername(username);
        return this;
    }

    public UserBuilder withEmail(String email) throws IllegalArgumentException {
        if (email == null || email.isBlank())
            return this;

        isEmailValid(email);
        this.user.setEmail(email);
        return this;
    }

    public UserBuilder withPassword(String password) throws IllegalArgumentException {
        isPasswordValid(password);
        String encryptedPassword = encryptPassword(password);
        this.user.setPassword(encryptedPassword);
        return this;
    }

    public UserBuilder withBirthDate(LocalDate birthDate) throws IllegalArgumentException {
        isBirthDateValid(birthDate);
        this.user.setBirthDate(birthDate);
        return this;
    }

    public UserBuilder withRegisterDto(RegisterRequest registerRequest) throws IllegalArgumentException {
        this.withUsername(registerRequest.username());
        this.withEmail(registerRequest.email());
        this.withPassword(registerRequest.password());
        this.withBirthDate(registerRequest.birthDate());

        return this;
    }

    public User build() {
        return this.user;
    }


    private void isUsernameValid(String username) throws IllegalArgumentException {
        final int MINIMUM_USERNAME_LENGTH = 4;

        if (repository.existsByUsername(username))
            throw new IllegalArgumentException(String.format("O 'Username': %s já está em uso", username));

        if (!this.isLengthValid(username, MINIMUM_USERNAME_LENGTH))
            throw new IllegalArgumentException(String.format("'Username' deve conter ao menos %d caracteres", MINIMUM_USERNAME_LENGTH));

        Pattern p = Pattern.compile("[\\w]+");
        if (!p.matcher(username).matches())
            throw new IllegalArgumentException("O 'Username' deve conter apenas caracteres alfanuméricos!");
    }

    private void isEmailValid(String email) {
        final String EMAIL_PATTERN = "^[\\w\\d_.]+@[\\w\\d.-]+\\.[a-zA-Z]{2,}$";
        if (!email.matches(EMAIL_PATTERN)) {
            throw new IllegalArgumentException("O 'E-Mail' digitado não é válido!");
        }
    }

    private void isPasswordValid(String password) {
        final int MINIMUM_PASSWORD_LENGTH = 8;
        final Pattern UPPERCASE_LETTERS = Pattern.compile("[A-Z]"),
                      LOWERCASE_LETTERS = Pattern.compile("[a-z]"),
                      NUMERIC_CHARS = Pattern.compile("\\d"),
                      SPECIAL_CHARS = Pattern.compile("[!@#\\$%^&*()_+{}\\[\\]:;<>,.?~\\\\/-]");
        final String ERROR_MESSAGE = "A 'Senha' deve conter ao menos";
        if (!this.isLengthValid(password, MINIMUM_PASSWORD_LENGTH))
            throw new IllegalArgumentException(String.format("%s %d caracteres", ERROR_MESSAGE, MINIMUM_PASSWORD_LENGTH));

        if (!UPPERCASE_LETTERS.matcher(password).find())
            throw new IllegalArgumentException(String.format("%s uma letra maiúscula!", ERROR_MESSAGE));

        if (!LOWERCASE_LETTERS.matcher(password).find())
            throw new IllegalArgumentException(String.format("%s uma letra minúscula!", ERROR_MESSAGE));

        if (!NUMERIC_CHARS.matcher(password).find())
            throw new IllegalArgumentException(String.format("%s um caractere numérico!", ERROR_MESSAGE));

        if (!SPECIAL_CHARS.matcher(password).find())
            throw new IllegalArgumentException(String.format("%s um caractere especial!", ERROR_MESSAGE));
    }

    private void isBirthDateValid(LocalDate birthDate) {
        final int MINIMUM_REQUIRED_AGE = 10;
        if (!(Period.between(birthDate, LocalDate.now()).getYears() >= MINIMUM_REQUIRED_AGE))
            throw new IllegalArgumentException(String.format("A idade mínima é: %d", MINIMUM_REQUIRED_AGE));
    }

    private boolean isLengthValid(String data, int length) {
        return data.length() >= length;
    }

    private String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
