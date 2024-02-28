package luan.moonvs.services;

import luan.moonvs.models.builders.UserBuilder;
import luan.moonvs.models.entities.User;
import luan.moonvs.models.requests.UserAccountRequest;
import luan.moonvs.models.responses.Response;
import luan.moonvs.models.responses.UserAuthData;
import luan.moonvs.repositories.UserRepository;
import luan.moonvs.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final UserRepository repository;

    @Autowired
    private AuthService(AuthenticationManager authManager, TokenService tokenService, UserRepository repository) {
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.repository = repository;
    }

    public Response<UserAuthData> login(String username, String password) {
        var usernameAndPassword = new UsernamePasswordAuthenticationToken(username, password);
        var auth = authManager.authenticate(usernameAndPassword);
        var user = (User) auth.getPrincipal();
        var token = tokenService.generateToken(user);

        var authData = new UserAuthData(user.getIdUser(), token);
        return new Response<>(HttpStatus.OK, authData);
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
