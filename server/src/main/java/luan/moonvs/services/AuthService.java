package luan.moonvs.services;

import luan.moonvs.models.builders.UserBuilder;
import luan.moonvs.models.entities.User;
import luan.moonvs.models.requests.AuthRequestDTO;
import luan.moonvs.models.requests.RegisterRequestDTO;
import luan.moonvs.models.responses.AuthResponseDTO;
import luan.moonvs.models.responses.RegisterResponseDTO;
import luan.moonvs.repositories.UserRepository;
import luan.moonvs.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final UserRepository repository;
    private final UserBuilder userBuilder;

    @Autowired
    public AuthService(AuthenticationManager authManager, TokenService tokenService, UserRepository repository, UserBuilder userBuilder) {
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.repository = repository;
        this.userBuilder = userBuilder;
    }

    public ResponseEntity<AuthResponseDTO> login(AuthRequestDTO authDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authDTO.username(), authDTO.password());
        var auth = authManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    public ResponseEntity<RegisterResponseDTO> register(RegisterRequestDTO registerDTO) {
        try {
            User user = userBuilder
                    .withRegisterDto(registerDTO)
                    .build();

            repository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponseDTO("Usuário cadastrado com sucesso!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterResponseDTO(e.getMessage()));
        }
    }
}
