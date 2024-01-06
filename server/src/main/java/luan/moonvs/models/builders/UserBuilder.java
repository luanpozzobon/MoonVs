package luan.moonvs.models.builders;

import lombok.NoArgsConstructor;
import luan.moonvs.models.entities.User;
import luan.moonvs.models.requests.RegisterRequest;
import luan.moonvs.models.requests.UserAccountRequest;
import luan.moonvs.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

@NoArgsConstructor
@Component
public class UserBuilder {
    private User user;
    private UserRepository repository;

    @Deprecated
    public UserBuilder(UserRepository repository) {
        this.repository = repository;
        this.user = new User();
    }

    @Deprecated
    public UserBuilder fromAuthUser(User authUser) {
        user = authUser.clone();
        return this;
    }

    public static UserBuilder create(UserRepository repository) {
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.repository = repository;
        userBuilder.user = new User();

        return userBuilder;
    }

    public static UserBuilder create(UserRepository repository, User authUser) {
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.repository = repository;
        userBuilder.user = new User(authUser);

        return userBuilder;
    }

    public static UserBuilder create(UserRepository repository, UserAccountRequest userAccount) {
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.repository = repository;
        userBuilder.user = new User();

        return userBuilder.withUsername(userAccount.username())
                .withEmail(userAccount.email())
                .withPassword(userAccount.password())
                .withBirthDate(userAccount.birthDate());
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

    @Deprecated
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
        final String USERNAME_USED = String.format("The username: %s is already taken!", username),
                     USERNAME_SHORT = String.format("The username must have at least %d characters!", MINIMUM_USERNAME_LENGTH),
                     INVALID_CHARACTERS = "The username must contain only alphanumeric characters!";

        if (repository.existsByUsername(username))
            throw new IllegalArgumentException(USERNAME_USED);

        if (!this.isLengthValid(username, MINIMUM_USERNAME_LENGTH))
            throw new IllegalArgumentException(USERNAME_SHORT);

        Pattern p = Pattern.compile("[\\w]+");
        if (!p.matcher(username).matches())
            throw new IllegalArgumentException(INVALID_CHARACTERS);
    }

    private void isEmailValid(String email) {
        final String INVALID_EMAIL = "The filled email is not valid!";
        final String EMAIL_PATTERN = "^[\\w\\d_.]+@[\\w\\d.-]+\\.[a-zA-Z]{2,}$";

        if (!email.matches(EMAIL_PATTERN)) {
            throw new IllegalArgumentException(INVALID_EMAIL);
        }
    }

    private void isPasswordValid(String password) {
        final int MINIMUM_PASSWORD_LENGTH = 8;
        final Pattern UPPERCASE_LETTERS = Pattern.compile("[A-Z]"),
                      LOWERCASE_LETTERS = Pattern.compile("[a-z]"),
                      NUMERIC_CHARS = Pattern.compile("\\d"),
                      SPECIAL_CHARS = Pattern.compile("[!@#\\$%^&*()_+{}\\[\\]:;<>,.?~\\\\/-]");

        final String ERROR_MESSAGE = "The password must contain at least %s %s",
                     PASSWORD_SHORT = String.format(ERROR_MESSAGE, MINIMUM_PASSWORD_LENGTH, "characters"),
                     MISSING_UPPERCASE = String.format(ERROR_MESSAGE, "one uppercase", "letter"),
                     MISSING_LOWERCASE = String.format(ERROR_MESSAGE, "one lowercase", "letter"),
                     MISSING_NUMBER = String.format(ERROR_MESSAGE, "one numeric", "character"),
                     MISSING_SPECIAL = String.format(ERROR_MESSAGE, "one special", "character");

        if (!this.isLengthValid(password, MINIMUM_PASSWORD_LENGTH))
            throw new IllegalArgumentException(PASSWORD_SHORT);

        if (!UPPERCASE_LETTERS.matcher(password).find())
            throw new IllegalArgumentException(MISSING_UPPERCASE);

        if (!LOWERCASE_LETTERS.matcher(password).find())
            throw new IllegalArgumentException(MISSING_LOWERCASE);

        if (!NUMERIC_CHARS.matcher(password).find())
            throw new IllegalArgumentException(MISSING_NUMBER);

        if (!SPECIAL_CHARS.matcher(password).find())
            throw new IllegalArgumentException(MISSING_SPECIAL);
    }

    private void isBirthDateValid(LocalDate birthDate) {
        final int MINIMUM_REQUIRED_AGE = 10;
        final String TOO_YOUNG = "The required age is %d years old";

        if (!(Period.between(birthDate, LocalDate.now()).getYears() >= MINIMUM_REQUIRED_AGE))
            throw new IllegalArgumentException(TOO_YOUNG);
    }

    private boolean isLengthValid(String data, int length) {
        return data.length() >= length;
    }

    private String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
