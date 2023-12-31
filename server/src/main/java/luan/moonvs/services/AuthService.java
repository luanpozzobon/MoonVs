package luan.moonvs.services;

import luan.moonvs.models.builders.UserBuilder;
import luan.moonvs.models.entities.User;
import luan.moonvs.models.requests.AuthRequest;
import luan.moonvs.models.requests.RegisterRequest;
import luan.moonvs.models.responses.AuthResponse;
import luan.moonvs.models.responses.RegisterResponse;
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
    private AuthService(AuthenticationManager authManager, TokenService tokenService, UserRepository repository, UserBuilder userBuilder) {
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.repository = repository;
        this.userBuilder = userBuilder;
    }

    public ResponseEntity<AuthResponse> login(AuthRequest authDTO) {
        if ((authDTO.username() == null || authDTO.username().isBlank())
        ||  (authDTO.password() == null || authDTO.password().isBlank()))
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();

        var usernamePassword = new UsernamePasswordAuthenticationToken(authDTO.username(), authDTO.password());
        var auth = authManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new AuthResponse(token));
    }

    public ResponseEntity<?> register(RegisterRequest registerDTO) {
        try {
            User user = userBuilder
                    .withRegisterDto(registerDTO)
                    .build();

            repository.save(user);
            return login(new AuthRequest(registerDTO.username(), registerDTO.password()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new RegisterResponse(e.getMessage()));
        }
    }
}
