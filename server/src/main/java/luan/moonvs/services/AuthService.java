package luan.moonvs.services;

import luan.moonvs.models.builders.UserBuilder;
import luan.moonvs.models.entities.User;
import luan.moonvs.models.requests.AuthRequest;
import luan.moonvs.models.requests.RegisterRequest;
import luan.moonvs.models.requests.UserAccountRequest;
import luan.moonvs.models.responses.*;
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

    @Deprecated
    private final UserBuilder userBuilder;

    @Deprecated
    @Autowired
    private AuthService(AuthenticationManager authManager, TokenService tokenService, UserRepository repository, UserBuilder userBuilder) {
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.repository = repository;
        this.userBuilder = userBuilder;
    }

    @Deprecated
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

    public Response<UserAuthData> login(String username, String password) {
        var usernameAndPassword = new UsernamePasswordAuthenticationToken(username, password);
        var auth = authManager.authenticate(usernameAndPassword);
        var user = (User) auth.getPrincipal();
        var token = tokenService.generateToken(user);

        var authData = new UserAuthData(user.getIdUser(), token);
        return new Response<>(HttpStatus.OK, authData);
    }

    @Deprecated
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

    public Response<UserAuthData> register(UserAccountRequest userAccount) {
        try {
            User user = UserBuilder.create(repository, userAccount).build();

            repository.save(user);
            var response = login(userAccount.username(), userAccount.password());

            return new Response<>(HttpStatus.OK, response.entity());
        } catch (IllegalArgumentException e) {
            return new Response<>(HttpStatus.BAD_REQUEST, new UserAuthData(), e.getMessage());
        }
    }
}
