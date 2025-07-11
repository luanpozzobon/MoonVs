package lpz.moonvs.api.auth;

import lpz.moonvs.api.auth.input.LoginInput;
import lpz.moonvs.api.auth.input.RegisterInput;
import lpz.moonvs.application.auth.command.LoginCommand;
import lpz.moonvs.application.auth.command.RegisterCommand;
import lpz.moonvs.application.auth.output.LoginOutput;
import lpz.moonvs.application.auth.output.RegisterOutput;
import lpz.moonvs.application.auth.usecase.LoginUseCase;
import lpz.moonvs.application.auth.usecase.RegisterUseCase;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.infra.config.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/auth")
class AuthController implements IAuthController {
    private final TokenService tokenService;
    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;

    @Autowired
    public AuthController(final TokenService tokenService,
                          final RegisterUseCase registerUseCase,
                          final LoginUseCase loginUseCase) {
        this.tokenService = tokenService;
        this.registerUseCase = registerUseCase;
        this.loginUseCase = loginUseCase;
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<RegisterOutput> register(@RequestBody final RegisterInput input) {
        final RegisterOutput output = this.registerUseCase.execute(
                new RegisterCommand(input.email(), input.username(), input.password())
        );
        final URI uri = URI.create("/users/" + output.id());

        final var cookie = tokenService.generateCookieToken(Id.from(output.id()));

        return ResponseEntity
                .created(uri)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(output);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginOutput> login(@RequestBody final LoginInput input) {
        final LoginOutput output = this.loginUseCase.execute(
                new LoginCommand(input.username(), input.password())
        );

        final var cookie = tokenService.generateCookieToken(Id.from(output.id()));

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(output);
    }
}
