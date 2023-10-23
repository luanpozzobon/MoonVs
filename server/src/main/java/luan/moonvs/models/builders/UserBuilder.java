package luan.moonvs.models.builders;

import lombok.NoArgsConstructor;
import luan.moonvs.models.entities.User;
import luan.moonvs.models.requests.RegisterRequestDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;
import java.util.regex.Pattern;

@NoArgsConstructor
public class UserBuilder {
    private UUID id;
    private String username;
    private String email;
    private String password;
    private String confirmedPassword;
    private LocalDate birthDate;

    private UserBuilder(RegisterRequestDTO registerDTO) {
        this.username = registerDTO.username();
        this.email = registerDTO.email();
        this.password = registerDTO.password();
        this.confirmedPassword = registerDTO.confirmedPassword();
        this.birthDate = registerDTO.birthDate();
    }

    public User build() {
        return new User();
    }

    public User build(RegisterRequestDTO registerDTO) {
        var userBuilder = new UserBuilder(registerDTO);
        if (!userBuilder.isUserValid())
            throw new IllegalArgumentException("Ao menos um dos dados inseridos não é valido!");
        userBuilder.password = userBuilder.encryptPassword();
        return new User(userBuilder.username, userBuilder.email, userBuilder.password, userBuilder.birthDate);
    }

    private boolean isUserValid() {
        if (!this.isUsernameValid())
            return false;
        if (!this.isEmailValid())
            return false;
        if (!this.isPasswordValid())
            return false;

        return this.isBirthDateValid();
    }

    private boolean isUsernameValid() {
        final int MINIMUM_USERNAME_LENGTH = 4;
        if (!this.isLengthValid(this.username, MINIMUM_USERNAME_LENGTH))
            return false;

        Pattern p = Pattern.compile("[\\w]+");
        return p.matcher(this.username).matches();
    }

    private boolean isEmailValid() {
        return this.email.contains("@");
        // TODO - Implementar outras validações de E-Mail
    }

    private boolean isPasswordValid() {
        final int MINIMUM_PASSWORD_LENGTH = 8;
        if (!this.isLengthValid(this.password, MINIMUM_PASSWORD_LENGTH))
            return false;

        // TODO - Implementar outras validações de senha

        return this.password.equals(this.confirmedPassword);
    }

    private boolean isBirthDateValid() {
        return Period.between(this.birthDate, LocalDate.now()).getYears() >= 10;
    }

    private boolean isLengthValid(String data, int length) {
        return data.length() >= length;
    }

    private String encryptPassword() {
        return new BCryptPasswordEncoder().encode(this.password);
    }
}
