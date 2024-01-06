package luan.moonvs.controllers;

import luan.moonvs.models.requests.AuthRequest;
import luan.moonvs.models.requests.UserAccountRequest;
import luan.moonvs.models.responses.AuthResponse;
import luan.moonvs.models.requests.RegisterRequest;
import luan.moonvs.models.responses.UserAccountResponse;
import luan.moonvs.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService service;

    @Autowired
    private AuthController(AuthService service) {
        this.service = service;
    }

    @Deprecated
    @PostMapping( { "/", "/login" } )
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authDTO) {
        return service.login(authDTO);
    }

    @PostMapping( { "/sign-in" } )
    public ResponseEntity<AuthResponse> login(@RequestBody UserAccountRequest userAccount) {
        if ((userAccount.username() == null || userAccount.username().isBlank())
        ||  (userAccount.password() == null || userAccount.password().isBlank()))
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();

        String token = service.login(userAccount.username(), userAccount.password());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new AuthResponse(token));
    }

    @Deprecated
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerDTO) {
        return service.register(registerDTO);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(@RequestBody UserAccountRequest userAccount) {
        UserAccountResponse response = service.register(userAccount);

        return ResponseEntity
                .status(response.status())
                .body(new AuthResponse(response.message()));
    }
}
