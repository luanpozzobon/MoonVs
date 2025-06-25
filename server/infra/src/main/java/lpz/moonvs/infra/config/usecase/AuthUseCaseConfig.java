package lpz.moonvs.infra.config.usecase;

import lpz.moonvs.application.auth.usecase.LoginUseCase;
import lpz.moonvs.application.auth.usecase.RegisterUseCase;
import lpz.moonvs.domain.auth.contracts.IPasswordEncryptor;
import lpz.moonvs.domain.auth.contracts.IUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthUseCaseConfig {
    @Bean
    public RegisterUseCase createRegisterUseCase(final IUserRepository userRepository,
                                                 final IPasswordEncryptor passwordEncryptor) {
        return new RegisterUseCase(userRepository, passwordEncryptor);
    }

    @Bean
    public LoginUseCase createLoginUseCase(final IUserRepository userRepository,
                                           final IPasswordEncryptor passwordEncryptor) {
        return new LoginUseCase(userRepository, passwordEncryptor);
    }
}
