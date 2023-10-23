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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final UserRepository repository;

    @Autowired
    public AuthService(AuthenticationManager authManager, TokenService tokenService, UserRepository repository) {
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.repository = repository;
    }

    public ResponseEntity<AuthResponseDTO> login(AuthRequestDTO authDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authDTO.login(), authDTO.password());
        var auth = authManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    public ResponseEntity<RegisterResponseDTO> register(RegisterRequestDTO registerDTO) {
        if (repository.findByUsername(registerDTO.username()) != null)
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Já existe um usuário com este 'Username'!"));

        try {
            User user = new UserBuilder().build(registerDTO);
            repository.save(user);
            return ResponseEntity.ok().body(new RegisterResponseDTO("Usuário cadastrado com sucesso!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RegisterResponseDTO(e.getMessage()));
        }
    }
}
